package br.com.espacoalcancar.espaco_alcancar_app_api.applications.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.dto.ResultsRequestDTO;
import br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.dto.SensoryProfileRequest;
import br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.dto.SensoryProfileResponse;
import br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.entities.SensoryProfileEntity;
import br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.entities.SensoryProfileType;
import br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.entities.Status;
import br.com.espacoalcancar.espaco_alcancar_app_api.applications.repositories.SensoryProfileRepository;
import br.com.espacoalcancar.espaco_alcancar_app_api.professional.models.entities.ProfessionalEntity;
import br.com.espacoalcancar.espaco_alcancar_app_api.professional.services.ProfessionalService;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.entities.ChildEntity;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.repositories.ChildRepository;

@Service
public class SensoryProfileService {

  @Autowired
  private SensoryProfileRepository sensoryProfileRepository;

  @Autowired
  private ChildRepository childRepository;

  @Autowired
  private ProfessionalService professionalService;

  // Criar novo perfil sensorial
  public UUID create(SensoryProfileRequest request) {

    ChildEntity child = childRepository.findById(request.getChildId())
        .orElseThrow(() -> new UsernameNotFoundException("Dependente não encontrado."));
    SensoryProfileEntity sensoryProfile = new SensoryProfileEntity();
    sensoryProfile.setProfessional(professionalService.getCurrentProfessional());
    sensoryProfile.setStatus(Status.UNFILLED);
    sensoryProfile.setChild(child);
    sensoryProfile.setProfileType(request.getProfileType());
    sensoryProfile.setCreatedAt(LocalDateTime.now());
    sensoryProfile.setUpdatedAt(LocalDateTime.now());
    sensoryProfile.setResultsOfSensoryProfile("");
    sensoryProfile = sensoryProfileRepository.save(sensoryProfile);
    return sensoryProfile.getId();
  }

  // Listar todos os perfis sensoriais
  public List<SensoryProfileEntity> listAll() {
    return sensoryProfileRepository.findAll();
  }

  // Listar todos os perfis sensoriais de um profissional logado
  public List<SensoryProfileResponse> listAllByProfessional() {
    ProfessionalEntity professional = professionalService.getCurrentProfessional();

    SensoryProfileResponse instant = new SensoryProfileResponse();
    List<SensoryProfileResponse> response = new ArrayList<>();
    List<SensoryProfileEntity> sensoryProfilesEntity = sensoryProfileRepository
        .findAllByProfessionalId(professional.getId());

    sensoryProfilesEntity.forEach(entity -> {
      instant.setId(entity.getId());
      instant.setChildName(entity.getChild().getName());
      instant.setProfileType(entity.getProfileType().toString());
      instant.setStatus(entity.getStatus().toString());
      instant.setCreatedAt(entity.getCreatedAt().toString());
      instant.setUpdatedAt(entity.getUpdatedAt().toString());
      instant.setResultsOfSensoryProfile(entity.getResultsOfSensoryProfile());
      response.add(instant);
    });
    return response;
  }

  // Preencher um perfil sensorial
  public void fillOut(ResultsRequestDTO results) {
    SensoryProfileEntity sensoryProfile = sensoryProfileRepository.findById(results.getSensoryProfileId())
        .orElseThrow(() -> new UsernameNotFoundException("Perfil sensorial não encontrado."));

    // Verificando se o perfil sensorial foi preenchido completamente
    var numberOfAnswersFilled = results.getAnswers().length();
    if (numberOfAnswersFilled == 0) {
      sensoryProfile.setStatus(Status.UNFILLED);
    } else if (numberOfAnswersFilled == getExpectedQuestions(sensoryProfile.getProfileType())) {
      sensoryProfile.setStatus(Status.FINISHED);
    } else if (numberOfAnswersFilled > 0
        && numberOfAnswersFilled < getExpectedQuestions(sensoryProfile.getProfileType())) {
      sensoryProfile.setStatus(Status.STARTED);
    }

    sensoryProfile.setResultsOfSensoryProfile(results.getAnswers());
    sensoryProfile.setUpdatedAt(LocalDateTime.now());
    sensoryProfileRepository.save(sensoryProfile);
  }

  // Resultados de um perfil sensorial
  public Map<String, Integer> calculateSensoryProfile(ResultsRequestDTO request,
      SensoryProfileType sensoryProfileType) {
    List<Integer> valuesToCalc = new ArrayList<>();
    Map<String, Integer> finalResults = new HashMap<>();
    for (char c : request.getAnswers().toCharArray()) {
      valuesToCalc.add(Character.getNumericValue(c));
    }

    if (sensoryProfileType == SensoryProfileType.UNTIL_THREE_YEARS) {
      Integer exploracao = valuesToCalc.get(17) + valuesToCalc.get(18) + valuesToCalc.get(19) + valuesToCalc.get(31)
          + valuesToCalc.get(35) + valuesToCalc.get(36) + valuesToCalc.get(37);

      Integer esquiva = valuesToCalc.get(2) + valuesToCalc.get(9) + valuesToCalc.get(26) + valuesToCalc.get(27)
          + valuesToCalc.get(28) + valuesToCalc.get(32) + valuesToCalc.get(34) + valuesToCalc.get(41)
          + valuesToCalc.get(48) + valuesToCalc.get(52) + valuesToCalc.get(53);

      Integer sensibilidade = valuesToCalc.get(0) + valuesToCalc.get(1) + valuesToCalc.get(12) + valuesToCalc.get(15)
          + valuesToCalc.get(25) + valuesToCalc.get(30) + valuesToCalc.get(33) + valuesToCalc.get(38)
          + valuesToCalc.get(40) + valuesToCalc.get(43) + valuesToCalc.get(45) + valuesToCalc.get(47)
          + valuesToCalc.get(51);

      Integer observacao = valuesToCalc.get(8) + valuesToCalc.get(10) + valuesToCalc.get(11) + valuesToCalc.get(13)
          + valuesToCalc.get(14) + valuesToCalc.get(22) + valuesToCalc.get(23) + valuesToCalc.get(24)
          + valuesToCalc.get(29) + valuesToCalc.get(39) + valuesToCalc.get(44);

      Integer geral = valuesToCalc.get(0) + valuesToCalc.get(1) + valuesToCalc.get(2) + valuesToCalc.get(3)
          + valuesToCalc.get(4) + valuesToCalc.get(5) + valuesToCalc.get(6) + valuesToCalc.get(7)
          + valuesToCalc.get(8) + valuesToCalc.get(9);

      Integer auditivo = valuesToCalc.get(10) + valuesToCalc.get(11) + valuesToCalc.get(12) + valuesToCalc.get(13)
          + valuesToCalc.get(14) + valuesToCalc.get(15) + valuesToCalc.get(16);

      Integer visual = valuesToCalc.get(17) + valuesToCalc.get(18) + valuesToCalc.get(19) + valuesToCalc.get(20)
          + valuesToCalc.get(21) + valuesToCalc.get(22) + valuesToCalc.get(23) + valuesToCalc.get(24);

      Integer tato = valuesToCalc.get(25) + valuesToCalc.get(26) + valuesToCalc.get(27) + valuesToCalc.get(28)
          + valuesToCalc.get(29) + valuesToCalc.get(30) + valuesToCalc.get(31) + valuesToCalc.get(32)
          + valuesToCalc.get(33) + valuesToCalc.get(34);

      Integer movimentos = valuesToCalc.get(35) + valuesToCalc.get(36) + valuesToCalc.get(37) + valuesToCalc.get(38)
          + valuesToCalc.get(39) + valuesToCalc.get(40);

      Integer sensibilidadeOral = valuesToCalc.get(41) + valuesToCalc.get(42) + valuesToCalc.get(43)
          + valuesToCalc.get(44)
          + valuesToCalc.get(45) + valuesToCalc.get(46) + valuesToCalc.get(47);

      Integer comportamental = valuesToCalc.get(48) + valuesToCalc.get(49) + valuesToCalc.get(50) + valuesToCalc.get(51)
          + valuesToCalc.get(52) + valuesToCalc.get(53);

      finalResults.put("exploracao", exploracao);
      finalResults.put("esquiva", esquiva);
      finalResults.put("sensibilidade", sensibilidade);
      finalResults.put("observacao", observacao);
      finalResults.put("geral", geral);
      finalResults.put("auditivo", auditivo);
      finalResults.put("visual", visual);
      finalResults.put("tato", tato);
      finalResults.put("movimentos", movimentos);
      finalResults.put("sensibilidadeOral", sensibilidadeOral);
      finalResults.put("comportamental", comportamental);

      return finalResults;
    } else if (sensoryProfileType == SensoryProfileType.MORE_THAN_THREE_YEARS) {
      Integer exploracao = valuesToCalc.get(13) + valuesToCalc.get(20) + valuesToCalc.get(21) + valuesToCalc.get(24)
          + valuesToCalc.get(26)
          + valuesToCalc.get(27) + valuesToCalc.get(29) + valuesToCalc.get(30) + valuesToCalc.get(31)
          + valuesToCalc.get(40) + valuesToCalc.get(47)
          + valuesToCalc.get(48) + valuesToCalc.get(49) + valuesToCalc.get(50) + valuesToCalc.get(54)
          + valuesToCalc.get(55) + valuesToCalc.get(59)
          + valuesToCalc.get(81) + valuesToCalc.get(82);

      Integer esquiva = valuesToCalc.get(0) + valuesToCalc.get(1) + valuesToCalc.get(4) + valuesToCalc.get(14)
          + valuesToCalc.get(17)
          + valuesToCalc.get(57) + valuesToCalc.get(58) + valuesToCalc.get(60) + valuesToCalc.get(62)
          + valuesToCalc.get(63) + valuesToCalc.get(64)
          + valuesToCalc.get(65) + valuesToCalc.get(66) + valuesToCalc.get(67) + valuesToCalc.get(69)
          + valuesToCalc.get(70) + valuesToCalc.get(71)
          + valuesToCalc.get(73) + valuesToCalc.get(74) + valuesToCalc.get(80);

      Integer sensibilidade = valuesToCalc.get(2) + valuesToCalc.get(3) + valuesToCalc.get(5) + valuesToCalc.get(6)
          + valuesToCalc.get(8)
          + valuesToCalc.get(12) + valuesToCalc.get(15) + valuesToCalc.get(18) + valuesToCalc.get(19)
          + valuesToCalc.get(43)
          + valuesToCalc.get(44) + valuesToCalc.get(45) + valuesToCalc.get(46) + valuesToCalc.get(51)
          + valuesToCalc.get(68)
          + valuesToCalc.get(72) + valuesToCalc.get(76) + valuesToCalc.get(77) + valuesToCalc.get(83);

      Integer observacao = valuesToCalc.get(7) + valuesToCalc.get(11) + valuesToCalc.get(22) + valuesToCalc.get(23)
          + valuesToCalc.get(25)
          + valuesToCalc.get(32) + valuesToCalc.get(33) + valuesToCalc.get(34) + valuesToCalc.get(35)
          + valuesToCalc.get(36)
          + valuesToCalc.get(37) + valuesToCalc.get(38) + valuesToCalc.get(39) + valuesToCalc.get(52)
          + valuesToCalc.get(53)
          + valuesToCalc.get(56) + valuesToCalc.get(61) + valuesToCalc.get(75) + valuesToCalc.get(78)
          + valuesToCalc.get(79)
          + valuesToCalc.get(84) + valuesToCalc.get(85);

      Integer auditivo = valuesToCalc.get(0) + valuesToCalc.get(1) + valuesToCalc.get(2) + valuesToCalc.get(3)
          + valuesToCalc.get(4)
          + valuesToCalc.get(5) +
          valuesToCalc.get(6) + valuesToCalc.get(7);

      Integer visual = valuesToCalc.get(8) + valuesToCalc.get(9) + valuesToCalc.get(10) + valuesToCalc.get(11)
          + valuesToCalc.get(12)
          + valuesToCalc.get(13) +
          valuesToCalc.get(14);

      Integer tato = valuesToCalc.get(15) + valuesToCalc.get(16) + valuesToCalc.get(17) + valuesToCalc.get(18)
          + valuesToCalc.get(19)
          + valuesToCalc.get(20) +
          valuesToCalc.get(21) + valuesToCalc.get(22) + valuesToCalc.get(23) + valuesToCalc.get(24)
          + valuesToCalc.get(25);

      Integer movimentos = valuesToCalc.get(26) + valuesToCalc.get(27) + valuesToCalc.get(28) + valuesToCalc.get(29)
          + valuesToCalc.get(30)
          + valuesToCalc.get(31) +
          valuesToCalc.get(32) + valuesToCalc.get(33);
      Integer posicaoDoCorpo = valuesToCalc.get(34) + valuesToCalc.get(35) + valuesToCalc.get(36) + valuesToCalc.get(37)
          + valuesToCalc.get(38)
          + valuesToCalc.get(39) +
          valuesToCalc.get(40) + valuesToCalc.get(41);
      Integer oral = valuesToCalc.get(42) + valuesToCalc.get(43) + valuesToCalc.get(44) + valuesToCalc.get(45)
          + valuesToCalc.get(46)
          + valuesToCalc.get(47) + valuesToCalc.get(48) + valuesToCalc.get(49) + valuesToCalc.get(50)
          + valuesToCalc.get(51);
      Integer conduta = valuesToCalc.get(52) + valuesToCalc.get(53) + valuesToCalc.get(54) + valuesToCalc.get(55)
          + valuesToCalc.get(56)
          + valuesToCalc.get(57) + valuesToCalc.get(58) + valuesToCalc.get(59) + valuesToCalc.get(60);
      Integer socioemocional = valuesToCalc.get(61) + valuesToCalc.get(62) + valuesToCalc.get(63) + valuesToCalc.get(64)
          + valuesToCalc.get(65)
          + valuesToCalc.get(66) + valuesToCalc.get(67) + valuesToCalc.get(68) + valuesToCalc.get(69)
          + valuesToCalc.get(70) + valuesToCalc.get(71)
          + valuesToCalc.get(72)
          + valuesToCalc.get(73) + valuesToCalc.get(74);
      Integer atencao = valuesToCalc.get(75) + valuesToCalc.get(76) + valuesToCalc.get(77) + valuesToCalc.get(78)
          + valuesToCalc.get(79)
          + valuesToCalc.get(80) +
          valuesToCalc.get(81) + valuesToCalc.get(82) + valuesToCalc.get(83) + valuesToCalc.get(84)
          + valuesToCalc.get(85);

      finalResults.put("exploracao", exploracao);
      finalResults.put("esquiva", esquiva);
      finalResults.put("sensibilidade", sensibilidade);
      finalResults.put("observacao", observacao);
      finalResults.put("auditivo", auditivo);
      finalResults.put("visual", visual);
      finalResults.put("tato", tato);
      finalResults.put("movimentos", movimentos);
      finalResults.put("posicaoDoCorpo", posicaoDoCorpo);
      finalResults.put("oral", oral);
      finalResults.put("conduta", conduta);
      finalResults.put("socioemocional", socioemocional);
      finalResults.put("atencao", atencao);
      return finalResults;
    } else {
      throw new IllegalArgumentException("Tipo de perfil sensorial incorreto.");
    }
  }

  // Obter perguntas de um perfil sensorial
  public List<String> getQuestions(SensoryProfileType sensoryProfileType) {
    List<String> questionsList = new ArrayList<>();
    if (sensoryProfileType == SensoryProfileType.UNTIL_THREE_YEARS) {

    } else if (sensoryProfileType == SensoryProfileType.MORE_THAN_THREE_YEARS) {
      questionsList.add(
          "Reage intensamente a sons inesperados ou barulhentos (por exemplo, sirenes, cachorro latindo, secador de cabelo).");
      questionsList.add("Coloca as mãos sobre os ouvidos para protegê-los do som.");
      questionsList.add("Tem dificuldade em concluir tarefas quando há música tocando ou a TV está ligada.");
      questionsList.add("Se distrai quando há muito barulho ao redor.");
      questionsList.add("Torna-se improdutivo(a) com ruídos de fundo (por exemplo, ventilador, geladeira).");
      questionsList.add("Para de prestar atenção em mim ou parece que me ignora.");
      questionsList.add("Parece não ouvir quando eu o(a) chamo por seu nome (mesmo com sua audição sendo normal).");
      questionsList.add("Prefere brincar ou fazer tarefas em condições de poucaluz.");
      questionsList.add("Prefere vestir-se com roupas de cores brilhantes ou estampadas");
      questionsList.add("Se diverte ao olhar para detalhes visuais em objetos.");
      questionsList.add("Precisa de ajuda para encontrar objetos que são óbvios para outros.");
      questionsList.add("Se incomoda mais com luzes brilhantes do que outras crianças da mesma idade.");
      questionsList.add("Observa as pessoas conforme ela se movem ao redor da sala.");
      questionsList.add(
          "Se incomoda com luzes brilhates (por exemplo, se esconde da luz solar que reluz através da janela do carro).");
      questionsList.add(
          "Mostra desconforto durante momentos de cuidado pessoal (por exemplo, briga ou chora durante o corte de cabelo, lavagem do rosto, corte das unhas das mãos).");
      questionsList.add("Se irrita com o uso de sapatos ou meias.");
      questionsList.add("Mostra uma resposta emocional ou agressiva ao ser tocado(a)");
      questionsList.add("Fica ansioso(a) quando fica de pé em proximidade a outros (por exemplo em uma fila)");
      questionsList.add("Esfrega ou coça uma parte do corpo que foi tocada");
      questionsList.add("Toca as pessoas ou objetos a ponto de incomodar outros.");
      questionsList.add(
          "Exibe a necessidade de tocar brinquedos, superfícies ou texturas (por exemplo, quer obter a sensação de tudo ao redor).");
      questionsList.add("Parece não ter consciência quando à dor");
      questionsList.add("Parece não ter consciência quanto a mudanças de temperatura");

    } else {
      throw new IllegalArgumentException("Tipo de perfil sensorial incorreto.");
    }
  }

  // Função auxiliar para obter o número esperado de perguntas com base no tipo de
  // perfil sensorial
  private int getExpectedQuestions(SensoryProfileType profileType) {
    switch (profileType) {
      case UNTIL_THREE_YEARS:
        return 54;
      case MORE_THAN_THREE_YEARS:
        return 86;
      default:
        throw new IllegalArgumentException("Tipo de perfil sensorial incorreto.");
    }
  }
}
