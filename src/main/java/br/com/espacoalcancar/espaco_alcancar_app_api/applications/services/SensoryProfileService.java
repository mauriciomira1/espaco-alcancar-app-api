package br.com.espacoalcancar.espaco_alcancar_app_api.applications.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.dto.ResultsOfSensoryProfileMoreThanThreeYearsDTO;
import br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.dto.ResultsOfSensoryProfileUntilThreeYearsDTO;
import br.com.espacoalcancar.espaco_alcancar_app_api.applications.exceptions.UnauthorizedHandlerException;
import br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.dto.SensoryProfileResponse;
import br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.dto.SensoryProfileTypeRequest;
import br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.entities.SensoryProfileEntity;
import br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.entities.SensoryProfileType;
import br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.entities.Status;
import br.com.espacoalcancar.espaco_alcancar_app_api.applications.repositories.SensoryProfileRepository;
import br.com.espacoalcancar.espaco_alcancar_app_api.professional.models.entities.ProfessionalEntity;
import br.com.espacoalcancar.espaco_alcancar_app_api.professional.services.ProfessionalService;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.dto.UserDashboardResponse;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.entities.ChildEntity;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.repositories.ChildRepository;
import java.time.LocalDate;
import java.time.Period;

@Service
public class SensoryProfileService {

        private static final Logger logger = LoggerFactory.getLogger(SensoryProfileService.class);

        @Autowired
        private SensoryProfileRepository sensoryProfileRepository;

        @Autowired
        private ChildRepository childRepository;

        @Autowired
        private ProfessionalService professionalService;

        // Criar novo perfil sensorial
        public UUID create(UUID childId) {

                ChildEntity child = childRepository.findById(childId)
                                .orElseThrow(() -> new UsernameNotFoundException("Dependente não encontrado."));

                // Calcular a idade da criança
                Integer childAge = Period.between(child.getBirth(), LocalDate.now()).getYears();

                // Definir o tipo de perfil sensorial com base na idade da criança
                SensoryProfileType profileType;
                if (childAge < 3) {
                        profileType = SensoryProfileType.UNTIL_THREE_YEARS;
                } else {
                        profileType = SensoryProfileType.MORE_THAN_THREE_YEARS;
                }

                SensoryProfileEntity sensoryProfile = new SensoryProfileEntity();
                sensoryProfile.setProfessional(professionalService.getCurrentProfessional());
                sensoryProfile.setStatus(Status.UNFILLED);
                sensoryProfile.setChild(child);
                sensoryProfile.setProfileType(profileType);
                sensoryProfile.setCreatedAt(LocalDateTime.now());
                sensoryProfile.setUpdatedAt(LocalDateTime.now());
                sensoryProfile.setResultsOfSensoryProfile("");
                sensoryProfile = sensoryProfileRepository.save(sensoryProfile);
                return sensoryProfile.getId();
        }

        // Listar todos os perfis sensoriais (perfil: admin)
        public List<SensoryProfileEntity> listAll() {
                return sensoryProfileRepository.findAll();
        }

        // Listar todos os perfis sensoriais de um profissional logado
        public List<SensoryProfileResponse> listAllByProfessional() {
                ProfessionalEntity professional = professionalService.getCurrentProfessional();

                List<SensoryProfileResponse> response = new ArrayList<>();
                List<SensoryProfileEntity> sensoryProfilesEntity = sensoryProfileRepository
                                .findAllByProfessionalId(professional.getId());

                sensoryProfilesEntity.forEach(entity -> {
                        SensoryProfileResponse spresponse = new SensoryProfileResponse();
                        spresponse.setId(entity.getId());
                        spresponse.setChildName(entity.getChild().getName());
                        spresponse.setChildId(entity.getChild().getId());
                        spresponse.setProfileType(entity.getProfileType().toString());
                        spresponse.setStatus(entity.getStatus().toString());
                        spresponse.setCreatedAt(entity.getCreatedAt().toString());
                        spresponse.setUpdatedAt(entity.getUpdatedAt().toString());
                        spresponse.setResultsOfSensoryProfile(entity.getResultsOfSensoryProfile());

                        response.add(spresponse);
                });
                return response;
        }

        // Listar todos os perfis sensoriais de uma criança (perfil: paciente)
        public List<SensoryProfileEntity> listAllByChild(UUID childId) {

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication == null || !authentication.isAuthenticated()) {
                        throw new UnauthorizedHandlerException("User is not authenticated");
                }

                var principal = (UserDashboardResponse) authentication.getPrincipal();
                UUID userId = principal.getId();

                List<SensoryProfileEntity> response = sensoryProfileRepository.findAllByChildId(childId);

                checkUserAuthorization(response, userId);

                return response;
        }

        private void checkUserAuthorization(List<SensoryProfileEntity> response, UUID userId) {
                response.forEach(child -> {
                        logger.info("Child User ID: {}", child.getChild().getUser().getId());
                        logger.info("Authenticated User ID: {}", userId);
                        logger.info("User ID Match: {}", child.getChild().getUser().getId().equals(userId));
                        if (!child.getChild().getUser().getId().equals(userId)) {
                                throw new UnauthorizedHandlerException(
                                                "User is not authorized to access this resource");
                        }
                });
        }

        // Resultados de um perfil sensorial para crianças menores de 3 anos
        public ResultsOfSensoryProfileUntilThreeYearsDTO calculateSensoryProfileUntilThreeYears(
                        String sensoryProfileId) {
                List<Integer> valuesToCalc = new ArrayList<>();

                SensoryProfileEntity sensoryProfile = sensoryProfileRepository
                                .findById(UUID.fromString(sensoryProfileId))
                                .orElseThrow(() -> new UsernameNotFoundException("Perfil sensorial não encontrado."));

                // Verificar se a criança realmente é menor de 3 anos
                Optional<ChildEntity> child = childRepository.findById(sensoryProfile.getChild().getId());

                Integer childAge = Period.between(child.get().getBirth(), LocalDate.now()).getYears();

                if (childAge >= 3) {
                        throw new UnauthorizedHandlerException("A criança não é menor de 3 anos.");
                }

                if (sensoryProfile.getResultsOfSensoryProfile().length() < 54) {
                        throw new UnauthorizedHandlerException("Perfil sensorial ainda não foi finalizado.");
                }

                for (char c : sensoryProfile.getResultsOfSensoryProfile().toCharArray()) {
                        valuesToCalc.add(Character.getNumericValue(c));
                }

                // Criação do objeto de resultados com exploração, esquiva, sensibilidade, etc.
                ResultsOfSensoryProfileUntilThreeYearsDTO results = new ResultsOfSensoryProfileUntilThreeYearsDTO();

                results.setExploracao(valuesToCalc.get(17) + valuesToCalc.get(18) + valuesToCalc.get(19)
                                + valuesToCalc.get(31)
                                + valuesToCalc.get(35) + valuesToCalc.get(36) + valuesToCalc.get(37));

                results.setEsquiva(valuesToCalc.get(2) + valuesToCalc.get(9) + valuesToCalc.get(26)
                                + valuesToCalc.get(27)
                                + valuesToCalc.get(28) + valuesToCalc.get(32) + valuesToCalc.get(34)
                                + valuesToCalc.get(41)
                                + valuesToCalc.get(48) + valuesToCalc.get(52) + valuesToCalc.get(53));

                results.setSensibilidade(valuesToCalc.get(0) + valuesToCalc.get(1) + valuesToCalc.get(12)
                                + valuesToCalc.get(15)
                                + valuesToCalc.get(25) + valuesToCalc.get(30) + valuesToCalc.get(33)
                                + valuesToCalc.get(38)
                                + valuesToCalc.get(40) + valuesToCalc.get(43) + valuesToCalc.get(45)
                                + valuesToCalc.get(47)
                                + valuesToCalc.get(51));

                results.setObservacao(valuesToCalc.get(8) + valuesToCalc.get(10) + valuesToCalc.get(11)
                                + valuesToCalc.get(13)
                                + valuesToCalc.get(14) + valuesToCalc.get(22) + valuesToCalc.get(23)
                                + valuesToCalc.get(24)
                                + valuesToCalc.get(29) + valuesToCalc.get(39) + valuesToCalc.get(44));

                results.setGeral(valuesToCalc.get(0) + valuesToCalc.get(1) + valuesToCalc.get(2)
                                + valuesToCalc.get(3)
                                + valuesToCalc.get(4) + valuesToCalc.get(5) + valuesToCalc.get(6)
                                + valuesToCalc.get(7)
                                + valuesToCalc.get(8) + valuesToCalc.get(9));

                results.setAuditivo(valuesToCalc.get(10) + valuesToCalc.get(11) + valuesToCalc.get(12)
                                + valuesToCalc.get(13)
                                + valuesToCalc.get(14) + valuesToCalc.get(15) + valuesToCalc.get(16));

                results.setVisual(valuesToCalc.get(17) + valuesToCalc.get(18) + valuesToCalc.get(19)
                                + valuesToCalc.get(20)
                                + valuesToCalc.get(21) + valuesToCalc.get(22) + valuesToCalc.get(23)
                                + valuesToCalc.get(24));

                results.setTato(valuesToCalc.get(25) + valuesToCalc.get(26) + valuesToCalc.get(27)
                                + valuesToCalc.get(28)
                                + valuesToCalc.get(29) + valuesToCalc.get(30) + valuesToCalc.get(31)
                                + valuesToCalc.get(32)
                                + valuesToCalc.get(33) + valuesToCalc.get(34));

                results.setMovimentos(valuesToCalc.get(35) + valuesToCalc.get(36) + valuesToCalc.get(37)
                                + valuesToCalc.get(38)
                                + valuesToCalc.get(39) + valuesToCalc.get(40));

                results.setSensibilidadeOral(valuesToCalc.get(41) + valuesToCalc.get(42) + valuesToCalc.get(43)
                                + valuesToCalc.get(44)
                                + valuesToCalc.get(45) + valuesToCalc.get(46) + valuesToCalc.get(47));

                results.setComportamental(valuesToCalc.get(48) + valuesToCalc.get(49) + valuesToCalc.get(50)
                                + valuesToCalc.get(51)
                                + valuesToCalc.get(52) + valuesToCalc.get(53));

                return results;

        }

        // Calcular perfil sensorial de acordo com idade da criança
        public Object calculateSensoryProfile(String sensoryProfileId) {
                SensoryProfileEntity sensoryProfile = sensoryProfileRepository
                                .findById(UUID.fromString(sensoryProfileId))
                                .orElseThrow(() -> new UsernameNotFoundException("Perfil sensorial não encontrado."));

                if (sensoryProfile.getProfileType() == SensoryProfileType.UNTIL_THREE_YEARS) {
                        return calculateSensoryProfileUntilThreeYears(sensoryProfileId);
                } else {
                        return calculateSensoryProfileMoreThanThreeYears(sensoryProfileId);
                }
        }

        // Resultados de um perfil sensorial para crianças maiores de 3 anos
        public ResultsOfSensoryProfileMoreThanThreeYearsDTO calculateSensoryProfileMoreThanThreeYears(
                        String sensoryProfileId) {
                List<Integer> valuesToCalc = new ArrayList<>();

                SensoryProfileEntity sensoryProfile = sensoryProfileRepository
                                .findById(UUID.fromString(sensoryProfileId))
                                .orElseThrow(() -> new UsernameNotFoundException("Perfil sensorial não encontrado."));

                // Verificar se a criança realmente é igual ou maior que 3 anos
                Optional<ChildEntity> child = childRepository.findById(sensoryProfile.getChild().getId());

                Integer childAge = Period.between(child.get().getBirth(), LocalDate.now()).getYears();

                if (childAge < 3) {
                        throw new UnauthorizedHandlerException("A criança não é igual ou maior de 3 anos.");
                }

                if (sensoryProfile.getResultsOfSensoryProfile().length() < 86) {
                        throw new UnauthorizedHandlerException("Perfil sensorial ainda não foi finalizado.");
                }

                for (char c : sensoryProfile.getResultsOfSensoryProfile().toCharArray()) {
                        valuesToCalc.add(Character.getNumericValue(c));
                }

                ResultsOfSensoryProfileMoreThanThreeYearsDTO results = new ResultsOfSensoryProfileMoreThanThreeYearsDTO();

                results.setExploracao(valuesToCalc.get(13) + valuesToCalc.get(20) + valuesToCalc.get(21)
                                + valuesToCalc.get(24)
                                + valuesToCalc.get(26)
                                + valuesToCalc.get(27) + valuesToCalc.get(29) + valuesToCalc.get(30)
                                + valuesToCalc.get(31)
                                + valuesToCalc.get(40) + valuesToCalc.get(47)
                                + valuesToCalc.get(48) + valuesToCalc.get(49) + valuesToCalc.get(50)
                                + valuesToCalc.get(54)
                                + valuesToCalc.get(55) + valuesToCalc.get(59)
                                + valuesToCalc.get(81) + valuesToCalc.get(82));

                results.setEsquiva(valuesToCalc.get(0) + valuesToCalc.get(1) + valuesToCalc.get(4)
                                + valuesToCalc.get(14)
                                + valuesToCalc.get(17)
                                + valuesToCalc.get(57) + valuesToCalc.get(58) + valuesToCalc.get(60)
                                + valuesToCalc.get(62)
                                + valuesToCalc.get(63) + valuesToCalc.get(64)
                                + valuesToCalc.get(65) + valuesToCalc.get(66) + valuesToCalc.get(67)
                                + valuesToCalc.get(69)
                                + valuesToCalc.get(70) + valuesToCalc.get(71)
                                + valuesToCalc.get(73) + valuesToCalc.get(74) + valuesToCalc.get(80));

                results.setSensibilidade(valuesToCalc.get(2) + valuesToCalc.get(3) + valuesToCalc.get(5)
                                + valuesToCalc.get(6)
                                + valuesToCalc.get(8)
                                + valuesToCalc.get(12) + valuesToCalc.get(15) + valuesToCalc.get(18)
                                + valuesToCalc.get(19)
                                + valuesToCalc.get(43)
                                + valuesToCalc.get(44) + valuesToCalc.get(45) + valuesToCalc.get(46)
                                + valuesToCalc.get(51)
                                + valuesToCalc.get(68)
                                + valuesToCalc.get(72) + valuesToCalc.get(76) + valuesToCalc.get(77)
                                + valuesToCalc.get(83));

                results.setObservacao(valuesToCalc.get(7) + valuesToCalc.get(11) + valuesToCalc.get(22)
                                + valuesToCalc.get(23)
                                + valuesToCalc.get(25)
                                + valuesToCalc.get(32) + valuesToCalc.get(33) + valuesToCalc.get(34)
                                + valuesToCalc.get(35)
                                + valuesToCalc.get(36)
                                + valuesToCalc.get(37) + valuesToCalc.get(38) + valuesToCalc.get(39)
                                + valuesToCalc.get(52)
                                + valuesToCalc.get(53)
                                + valuesToCalc.get(56) + valuesToCalc.get(61) + valuesToCalc.get(75)
                                + valuesToCalc.get(78)
                                + valuesToCalc.get(79)
                                + valuesToCalc.get(84) + valuesToCalc.get(85));

                results.setAuditivo(valuesToCalc.get(0) + valuesToCalc.get(1) + valuesToCalc.get(2)
                                + valuesToCalc.get(3)
                                + valuesToCalc.get(4)
                                + valuesToCalc.get(5) +
                                valuesToCalc.get(6) + valuesToCalc.get(7));

                results.setVisual(valuesToCalc.get(8) + valuesToCalc.get(9) + valuesToCalc.get(10)
                                + valuesToCalc.get(11)
                                + valuesToCalc.get(12)
                                + valuesToCalc.get(13));

                results.setTato(valuesToCalc.get(15) + valuesToCalc.get(16) + valuesToCalc.get(17)
                                + valuesToCalc.get(18)
                                + valuesToCalc.get(19)
                                + valuesToCalc.get(20) +
                                valuesToCalc.get(21) + valuesToCalc.get(22) + valuesToCalc.get(23)
                                + valuesToCalc.get(24)
                                + valuesToCalc.get(25));

                results.setMovimentos(valuesToCalc.get(26) + valuesToCalc.get(27) + valuesToCalc.get(28)
                                + valuesToCalc.get(29)
                                + valuesToCalc.get(30)
                                + valuesToCalc.get(31) +
                                valuesToCalc.get(32) + valuesToCalc.get(33));
                results.setPosicaoDoCorpo(valuesToCalc.get(34) + valuesToCalc.get(35) + valuesToCalc.get(36)
                                + valuesToCalc.get(37)
                                + valuesToCalc.get(38)
                                + valuesToCalc.get(39) +
                                valuesToCalc.get(40) + valuesToCalc.get(41));
                results.setOral(valuesToCalc.get(42) + valuesToCalc.get(43) + valuesToCalc.get(44)
                                + valuesToCalc.get(45)
                                + valuesToCalc.get(46)
                                + valuesToCalc.get(47) + valuesToCalc.get(48) + valuesToCalc.get(49)
                                + valuesToCalc.get(50)
                                + valuesToCalc.get(51));
                results.setConduta(valuesToCalc.get(52) + valuesToCalc.get(53) + valuesToCalc.get(54)
                                + valuesToCalc.get(55)
                                + valuesToCalc.get(56)
                                + valuesToCalc.get(57) + valuesToCalc.get(58) + valuesToCalc.get(59)
                                + valuesToCalc.get(60));
                results.setSocioemocional(valuesToCalc.get(61) + valuesToCalc.get(62) + valuesToCalc.get(63)
                                + valuesToCalc.get(64)
                                + valuesToCalc.get(65)
                                + valuesToCalc.get(66) + valuesToCalc.get(67) + valuesToCalc.get(68)
                                + valuesToCalc.get(69)
                                + valuesToCalc.get(70) + valuesToCalc.get(71)
                                + valuesToCalc.get(72)
                                + valuesToCalc.get(73) + valuesToCalc.get(74));
                results.setAtencao(valuesToCalc.get(75) + valuesToCalc.get(76) + valuesToCalc.get(77)
                                + valuesToCalc.get(78)
                                + valuesToCalc.get(79)
                                + valuesToCalc.get(80) +
                                valuesToCalc.get(81) + valuesToCalc.get(82) + valuesToCalc.get(83)
                                + valuesToCalc.get(84));
                return results;
        }

        // Obter perguntas de um perfil sensorial
        public List<String> getQuestions(SensoryProfileTypeRequest sensoryProfileTypeRequest) {
                List<String> questionsList = new ArrayList<>();
                SensoryProfileType sensoryProfileType = sensoryProfileTypeRequest.getSensoryProfileType();

                if (sensoryProfileType == SensoryProfileType.UNTIL_THREE_YEARS) {
                        questionsList.add("1. Precisa de uma rotina para permanecer satisfeito(a) ou calmo(a).");
                        questionsList.add("2. Age de uma forma que interfere nas programações e planos da família.");
                        questionsList.add("3. Resiste em brincar no meio de outras crianças.");
                        questionsList.add(
                                        "4. Leva mais tempo do que crianças da mesma idade para responder a perguntas ou ações.");
                        questionsList.add("5. Se afasta de situações.");
                        questionsList.add("6. Tem um padrão de sono imprevisível.");
                        questionsList.add("7. Tem um padrão de alimentação imprevisível.");
                        questionsList.add("8. É despertado(a) facilmente.");
                        questionsList.add("9. Não faz contato visual comigo durante interações no dia a dia.");
                        questionsList.add("10. Fica ansioso(a) em situações novas.");
                        questionsList.add("11. Presta atenção apenas se eu falar alto.");
                        questionsList.add("12. Presta atenção apenas quando eu o toco (e a audição é normal).");
                        questionsList.add(
                                        "13. Assusta-se facilmente com sons, em comparação com crianças da mesma idade (por exemplo, cachorro latindo, crianças gritando).");
                        questionsList.add("14. Se distrai em condições barulhentas.");
                        questionsList.add("15. Ignora sons, incluindo a minha voz.");
                        questionsList.add("16. Fica chateado(a) ou tenta escapar de condições barulhentas.");
                        questionsList.add("17. Leva bastante tempo para responder ao próprio nome.");
                        questionsList.add(
                                        "18. Gosta de olhar para objetos em movimento ou rotação (por exemplo, ventiladores de teto, brinquedos com rodas).");
                        questionsList.add("19. Gosta de olhar para objetos brilhantes.");
                        questionsList.add(
                                        "20. É atraído(a) por telas de TV ou de computador com desenhos intensamente coloridos, em ritmo acelerado.");
                        questionsList.add(
                                        "21. Se assusta com luzes brilhantes ou imprevisíveis (por exemplo, ao se deslocar de uma sala escura para uma sala clara).");
                        questionsList.add(
                                        "22. Se incomoda com luzes brilhantes (por exemplo, se esconde da luz solar que reluz através da janela do carro).");
                        questionsList.add(
                                        "23. Incomoda-se com luzes brilhantes mais do que outras crianças da mesma idade.");
                        questionsList.add("24. Empurra brinquedos de coloração brilhante para longe de si.");
                        questionsList.add("25. Não responde à sua imagem no espelho.");
                        questionsList.add("26. Fica incomodado(a) quando suas unhas são aparadas.");
                        questionsList.add("27. Resiste a ser abraçado(a).");
                        questionsList.add(
                                        "28. Incomoda-se ao se deslocar entre espaços com temperaturas muito diferentes (por exemplo, mais frio, mais morno).");
                        questionsList.add(
                                        "29. Afasta-se após ter contato com superfícies ásperas, frias ou grudentas (por exemplo, carpete, bancadas).");
                        questionsList.add("30. Esbarra em coisas, sem conseguir notar objetos ou pessoas no caminho.");
                        questionsList.add("31. Remove roupas ou resiste a vesti-las.");
                        questionsList.add("32. Gosta de brincar de espirrar água durante o banho ou na natação.");
                        questionsList.add("33. Incomoda-se se suas próprias roupas, mãos ou rosto estão sujos.");
                        questionsList.add(
                                        "34. Demonstra ansiedade ao caminhar ou engatinhar em certas superfícies (por exemplo, grama, areia, carpete, azulejos).");
                        questionsList.add("35. Afasta-se de toques inesperados.");
                        questionsList.add(
                                        "36. Gosta de atividade física (por exemplo, pular, ser erguido(a) bem alto).");
                        questionsList
                                        .add("37. Gosta de atividades rítmicas (por exemplo, balançar, ser embalado, passeios de carro).");
                        questionsList.add("38. Se arrisca ao se movimentar ou escalar.");
                        questionsList
                                        .add("39. Incomoda-se quando colocado(a) de barriga para cima (por exemplo, para ser trocado(a)).");
                        questionsList.add("40. Parece ter propensão para acidentes ou ser desastrado(a).");
                        questionsList
                                        .add("41. Reclama quando é movimentado(a) (por exemplo, ao caminhar, ao ser entregue para outra pessoa).");
                        questionsList
                                        .add("42. Mostra uma aversão evidente à maioria dos alimentos, com exceção de algumas opções.");
                        questionsList.add("43. Baba.");
                        questionsList.add("44. Prefere uma textura de alimentos (por exemplo, macio, crocante).");
                        questionsList.add("45. Bebe para se acalmar.");
                        questionsList.add("46. Tem ânsia de vômito com alimentos ou bebidas.");
                        questionsList.add("47. Mantém a comida nas bochechas antes de engolir.");
                        questionsList.add(
                                        "48. Apresenta dificuldade na transição do leite materno para a alimentação sólida.");
                        questionsList.add("49. Faz birra.");
                        questionsList.add("50. É muito apegado e dependente.");
                        questionsList.add("51. Se acalma somente quando é segurado(a) no colo.");
                        questionsList.add("52. Mostra inquietude ou irritabilidade.");
                        questionsList.add("53. Se incomoda com novos ambientes.");
                        questionsList.add(
                                        "54. Fica tão incomodado(a) em novos ambientes que se torna difícil de acalmá-lo(a).");

                        return questionsList;

                } else if (sensoryProfileType == SensoryProfileType.MORE_THAN_THREE_YEARS) {
                        questionsList.add(
                                        "1. Reage intensamente a sons inesperados ou barulhentos (por exemplo, sirenes, cachorro latindo, secador de cabelo).");
                        questionsList.add("2. Coloca as mãos sobre os ouvidos para protegê-los do som.");
                        questionsList.add(
                                        "3. Tem dificuldade em concluir tarefas quando há música tocando ou a TV está ligada.");
                        questionsList.add("4. Se distrai quando há muito barulho ao redor.");
                        questionsList.add(
                                        "5. Torna-se improdutivo(a) com ruídos de fundo (por exemplo, ventilador, geladeira).");
                        questionsList.add("6. Para de prestar atenção em mim ou parece que me ignora.");
                        questionsList
                                        .add("7. Parece não ouvir quando eu o(a) chamo por seu nome (mesmo com sua audição sendo normal).");
                        questionsList.add("8. Gosta de barulhos estranhos ou faz barulho(s) para se divertir.");
                        questionsList.add("9. Prefere brincar ou fazer tarefas em condições de pouca luz.");
                        questionsList.add("10. Prefere vestir-se com roupas de cores brilhantes ou estampadas.");
                        questionsList.add("11. Se diverte ao olhar para detalhes visuais em objetos.");
                        questionsList.add("12. Precisa de ajuda para encontrar objetos que são óbvios para outros.");
                        questionsList.add(
                                        "13. Se incomoda mais com luzes brilhantes do que outras crianças da mesma idade.");
                        questionsList.add("14. Observa as pessoas conforme ela se movem ao redor da sala.");
                        questionsList.add(
                                        "15. Se incomoda com luzes brilhantes (por exemplo, se esconde da luz solar que reluz através da janela do carro).");
                        questionsList.add(
                                        "16. Mostra desconforto durante momentos de cuidado pessoal (por exemplo, briga ou chora durante o corte de cabelo, lavagem do rosto, corte das unhas das mãos).");
                        questionsList.add("17. Se irrita com o uso de sapatos ou meias.");
                        questionsList.add("18. Mostra uma resposta emocional ou agressiva ao ser tocado(a).");
                        questionsList
                                        .add("19. Fica ansioso(a) quando fica de pé em proximidade a outros (por exemplo, em uma fila)");
                        questionsList.add("20. Esfrega ou coça uma parte do corpo que foi tocada");
                        questionsList.add("21. Toca as pessoas ou objetos a ponto de incomodar outros.");
                        questionsList.add(
                                        "22. Exibe a necessidade de tocar brinquedos, superfícies ou texturas (por exemplo, quer obter a sensação de tudo ao redor).");
                        questionsList.add("23. Parece não ter consciência quanto à dor");
                        questionsList.add("24. Parece não ter consciência quanto a mudanças de temperatura");
                        questionsList.add("25. Toca pessoas e objetos mais do que crianças da mesma idade.");
                        questionsList.add("26. Parece alheio(a) quanto ao fato de suas mãos ou face estarem sujas.");
                        questionsList.add(
                                        "27. Busca movimentar-se até o ponto que interfere com rotinas diárias (por exemplo, não consegue ficar quieto, demonstra inquietude).");
                        questionsList.add("28. Faz movimento de balançar a cadeira, no chão ou enquanto está em pé.");
                        questionsList.add(
                                        "29. Hesita subir ou descer calçadas ou degraus (por exemplo, é cauteloso, para antes de se movimentar).");
                        questionsList.add("30. Fica animado(a) durante tarefas que envolvem movimento.");
                        questionsList.add("31. Se arrisca ao se movimentar ou escalar de modo perigoso.");
                        questionsList.add(
                                        "32. Procura oportunidades para cair sem se importar com a própria segurança (por exemplo, cai de propósito).");
                        questionsList.add(
                                        "33. Perde o equilíbrio inesperadamente ao caminhar sobre uma superfície irregular.");
                        questionsList.add("34. Esbarra em coisas, sem conseguir notar objetos ou pessoas no caminho.");
                        questionsList.add("35. Move-se de modo rígido.");
                        questionsList
                                        .add("36. Fica cansado(a) facilmente, principalmente quando está em pé ou mantendo o corpo em uma posição.");
                        questionsList.add("37. Parece ter músculos fracos.");
                        questionsList
                                        .add("38. Se apoia para se sustentar (por exemplo, segura a cabeça com as mãos, apoia-se em uma parede.)");
                        questionsList.add(
                                        "39. Se segura a objetos, paredes ou corrimões mais do que crianças da mesma idade.");
                        questionsList.add("40. Ao andar, faz barulho como se os pés fossem pesados.");
                        questionsList.add("41. Se inclina para se apoiar em imóveis ou em outras pessoas.");
                        questionsList.add("42. Precisa de cobertores pesados para dormir.");
                        questionsList.add(
                                        "43. Fica com ânsia de vômito facilmente com certas texturas de alimentos ou utensílios alimentares na boca.");
                        questionsList
                                        .add("44. Rejeita certos gostos ou cheiros de comida que são, normalmente, parte da dieta das crianças.");
                        questionsList.add("45. Se alimenta somente de certos sabores (por exemplo, doce, salgado).");
                        questionsList.add("46. Limita-se quanto a certas texturas de alimentos.");
                        questionsList.add(
                                        "47. É exigente para comer, principalmente com relação às texturas de alimentos.");
                        questionsList.add("48. Cheira objetos não comestíveis.");
                        questionsList.add("49. Mostra uma forte preferência por certos sabores.");
                        questionsList.add("50. Deseja intensamente certos alimentos, gostos ou cheiros.");
                        questionsList.add("51. Coloca objetos na boca (por exemplo, lápis, mãos).");
                        questionsList.add("52. Morde a língua ou lábios mais do que as crianças da mesma idade.");
                        questionsList.add("53. Parece propenso(a) a acidentes.");
                        questionsList.add("54. Se apressa em atividades de colorir, escrever ou desenhar.");
                        questionsList.add(
                                        "55. Se expõe a riscos excessivos (por exemplo, sobe alto em uma árvore, salta de móveis altos) que comprometem sua própria segurança.");
                        questionsList.add("56. Parece ser mais ativo(a) do que crianças da mesma idade.");
                        questionsList.add(
                                        "57. Faz as coisas de uma maneira mais difícil do que necessário (por exemplo, perde tempo, move-se lentamente).");
                        questionsList.add("58. Pode ser teimoso(a) e não cooperativo(a).");
                        questionsList.add("59. Faz birra.");
                        questionsList.add("60. Parece se divertir quando cai.");
                        questionsList.add("61. Resiste ao contato visual comigo ou com outros.");
                        questionsList.add(
                                        "62. Parece ter baixa autoestima (por exemplo, dificuldade de gostar de si mesmo(a)).");
                        questionsList.add("63. Precisa de apoio positivo para enfrentar situações desafiadoras.");
                        questionsList.add("64. É sensível às críticas.");
                        questionsList.add("65. Possui medos definidos e previsíveis.");
                        questionsList.add("66. Se expressa sentindo-se como um fracasso.");
                        questionsList.add("67. É demasiadamente sério(a).");
                        questionsList.add(
                                        "68. Tem fortes explosões emocionais quando não consegue concluir uma tarefa.");
                        questionsList.add(
                                        "69. Tem dificuldade de interpretar linguagem corporal ou expressões faciais.");
                        questionsList.add("70. Fica frustrado(a) facilmente.");
                        questionsList.add("71. Possui medos que interferem nas rotinas diárias.");
                        questionsList.add("72. Fica angustiado(a) com mudanças nos planos, rotinas ou expectativas.");
                        questionsList.add(
                                        "73. Precisa de mais proteção contra acontecimentos da vida do que crianças da mesma idade (por exemplo, é indefeso(a) física ou emocionalmente).");
                        questionsList.add("74. Interage ou participa em grupos menos que crianças da mesma idade.");
                        questionsList.add("75. Tem dificuldade com amizades (por exemplo, fazer ou manter amigos).");
                        questionsList.add("76. Não faz contato visual comigo durante interações no dia a dia.");
                        questionsList.add("77. Tem dificuldade para prestar atenção.");
                        questionsList.add("78. Se desvia de tarefas para observar todas as ações na sala.");
                        questionsList.add(
                                        "79. Parece alheio(a) dentro de um ambiente ativo (por exemplo, não tem consciência quanto à atividade).");
                        questionsList.add("80. Olha fixamente, de maneira intensa, para objetos.");
                        questionsList.add("81. Olha fixamente, de maneira intensa, para as pessoas.");
                        questionsList.add("82. Observa todos conforme se movem ao redor da sala.");
                        questionsList.add("83. Muda de uma coisa para outra de modo a interferir com as atividades.");
                        questionsList.add("84. Perde coisas facilmente.");
                        questionsList.add(
                                        "85. Tem dificuldade para encontrar objetos em espaços cheios de coisas (por exemplo, sapatos em um quarto bagunçado, lápis na 'gaveta de bagunças').");
                        questionsList.add("86. Parece não se dar conta quando pessoas entram na sala.");

                        return questionsList;

                } else {
                        throw new IllegalArgumentException("Tipo de perfil sensorial incorreto.");
                }
        }

        // Função auxiliar para obter o número de perguntas com base no tipo de PS
        /*
         * private int getExpectedQuestions(SensoryProfileType profileType) {
         * switch (profileType) {
         * case UNTIL_THREE_YEARS:
         * return 54;
         * case MORE_THAN_THREE_YEARS:
         * return 86;
         * default:
         * throw new IllegalArgumentException("Tipo de perfil sensorial incorreto.");
         * }
         * }
         */
}
