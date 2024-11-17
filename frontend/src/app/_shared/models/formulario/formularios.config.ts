import { TipoFormulario } from "../tipos.formulario.enum";
import { Formulario } from "./formulario";

export const FORMULARIOS_CONFIG: { [key in TipoFormulario]: Formulario } = {
    [TipoFormulario.SEDENTARISMO]: {
        id: 1,
        tipo: TipoFormulario.SEDENTARISMO,
        titulo: 'Nível de Atividade Física e Comportamento Sedentário',
        descricao: 'Nível de Atividade Física e Comportamento Sedentário',
        etapas: [
            {
                titulo: 'Atividade Moderada',
                descricao: 'Você consegue realizá-la conversando com dificuldade enquanto se movimenta e não vai conseguir cantar.',
                perguntas: [
                    {
                        texto: 'Quanto tempo por dia você realiza atividades moderadas? (HH:MM)',
                        tipo: 'tempo',
                        resposta: '',
                        validacao: { required: true }
                    },
                    {
                        texto: 'Quantos dias por semana?',
                        tipo: 'numero',
                        resposta: '',
                        validacao: { required: true, min: 0, max: 7 }
                    }
                ]
            },
            {
                titulo: 'Atividade Vigorosa',
                descricao: 'Você não vai conseguir nem conversar. A sua respiração vai ser muito mais rápida que o normal e os batimentos do seu coração vão aumentar muito.',
                perguntas: [
                    {
                        texto: 'Quanto tempo por dia você realiza atividades vigorosas? (HH:MM)',
                        tipo: 'tempo',
                        resposta: '',
                        validacao: { required: true }
                    },
                    {
                        texto: 'Quantos dias por semana?',
                        tipo: 'numero',
                        resposta: '',
                        validacao: { required: true, min: 0, max: 7 }
                    }
                ]
            },
            {
                titulo: 'Comportamento Sedentário',
                descricao: 'Quanto tempo do seu dia, enquanto você está acordado, você gasta sentado, reclinado ou deitado, assistindo televisão, no celular, em frente ao computador, realizando trabalhos manuais, dirigindo ou lendo?',
                perguntas: [
                    {
                        texto: 'Quanto tempo por dia? (HH:MM)',
                        tipo: 'tempo',
                        resposta: '',
                        validacao: { required: true }
                    },
                    {
                        texto: 'Quantos dias por semana?',
                        tipo: 'numero',
                        resposta: '',
                        validacao: { required: true, min: 0, max: 7 }
                    }
                ]
            },
            {
              titulo: 'Atividade Leve',
              descricao: 'Avaliação de atividades físicas leves.',
              perguntas: [
                  {
                      texto: 'Quanto tempo por dia você realiza atividades leves? (como caminhar devagar ou alongar) (HH:MM)',
                      tipo: 'tempo',
                      resposta: '',
                      validacao: { required: true }
                  },
                  {
                      texto: 'Quantos dias por semana você realiza essas atividades?',
                      tipo: 'numero',
                      resposta: '',
                      validacao: { required: true, min: 0, max: 7 }
                  }
              ]
          },
          {
              titulo: 'Sedentarismo Adicional',
              descricao: 'Avaliação de comportamentos sedentários.',
              perguntas: [
                  {
                      texto: 'Durante quantas horas por dia você passa sentado assistindo TV ou no celular? (HH:MM)',
                      tipo: 'tempo',
                      resposta: '',
                      validacao: { required: true }
                  },
                  {
                      texto: 'Quantas vezes por semana você faz pequenas pausas para caminhar durante longos períodos sentado?',
                      tipo: 'numero',
                      resposta: '',
                      validacao: { required: true, min: 0, max: 7 }
                  }
              ]
          }
        ]
    },

    [TipoFormulario.PFS]: {
        id: 2,
        tipo: TipoFormulario.PFS,
        titulo: 'Formulário PFS',
        descricao: 'Avaliação do nível de fadiga física e mental após diferentes atividades',
        etapas: [
            {
                titulo: 'Escala de Fatigabilidade de Pittsburgh',
                descricao: 'Instruções:  Nas perguntas a seguir indique o nível de fadiga física e mental (ou seja, cansaço, exaustão) que você espera ou imagina sentir imediatamente após completar cada uma das dez atividades listadas. Para cada atividade (a-j), circule as respostas para fadiga física e mental entre 0 e 5, onde “0” é igual a nenhuma fadiga e “5” é igual a fadiga extrema.',
                perguntas: []
            },
            {
                titulo: 'Atividade 1',
                descricao: 'Classifique os níveis de fadiga ao caminhar à lazer por 30 minutos.',
                perguntas: [
                    {
                        texto: 'Fadiga Física',
                        tipo: 'range',
                        resposta: '',
                        validacao: {
                            min: 0,
                            max: 5,
                            required: true
                        }
                    },                    {
                        texto: 'Fadiga Mental',
                        tipo: 'range',
                        resposta: '',
                        validacao: {
                            min: 0,
                            max: 5,
                            required: true
                        }
                    }
                ]
            },
            {
                titulo: 'Atividade 2',
                descricao: 'Classifique os níveis de fadiga: Atividade doméstica leve por 1 hora (limpar, cozinhar, tirar o pó, assar, arrumar camas, lavar louça, regar plantas)',
                perguntas: [
                    {
                        texto: 'Fadiga Física',
                        tipo: 'range',
                        resposta: '',
                        validacao: {
                            min: 0,
                            max: 5,
                            required: true
                        }
                    },                    {
                        texto: 'Fadiga Mental',
                        tipo: 'range',
                        resposta: '',
                        validacao: {
                            min: 0,
                            max: 5,
                            required: true
                        }
                    }
                ]
            },
            {
                titulo: 'Atividade 3',
                descricao: 'Classifique os níveis de fadiga: Participar de atividade social por 1 hora (festa, jantar, centro de idosos, reunião com família/amigos, jogar cartas)',
                perguntas: [
                    {
                        texto: 'Fadiga Física',
                        tipo: 'range',
                        resposta: '',
                        validacao: {
                            min: 0,
                            max: 5,
                            required: true
                        }
                    },                    {
                        texto: 'Fadiga Mental',
                        tipo: 'range',
                        resposta: '',
                        validacao: {
                            min: 0,
                            max: 5,
                            required: true
                        }
                    }
                ]
            },
            {
                titulo: 'Atividade 4',
                descricao: 'Classifique os níveis de fadiga: Atividade de alta intensidade por 30 minutos (corrida, caminhada, ciclismo, natação, esportes com raquete, aparelhos aeróbicos, dança, zumba)',
                perguntas: [
                    {
                        texto: 'Fadiga Física',
                        tipo: 'range',
                        resposta: '',
                        validacao: {
                            min: 0,
                            max: 5,
                            required: true
                        }
                    },                    {
                        texto: 'Fadiga Mental',
                        tipo: 'range',
                        resposta: '',
                        validacao: {
                            min: 0,
                            max: 5,
                            required: true
                        }
                    }
                ]
            }
        ]
    },

    [TipoFormulario.IVCF20]: {
        id: 3,
        tipo: TipoFormulario.IVCF20,
        titulo: 'Índice de Vulnerabilidade Clínico-Funcional (IVCF-20)',
        descricao: 'Avaliação multidimensional do grau de vulnerabilidade clínica e funcional de idosos',
        etapas: [
            {
                titulo: 'Auto-percepção da Saúde',
                descricao: 'Avaliação geral da saúde comparada a outras pessoas da mesma idade.',
                perguntas: [
                    {
                        texto: 'Em geral, comparado com outras pessoas da sua idade, como você diria que está a sua saúde?',
                        tipo: 'radio',
                        opcoes: ['Excelente, boa ou muito boa', 'Regular ou ruim'],
                        resposta: '',
                        validacao: { required: true }
                    },

                ]
            },
            {
                titulo: 'Percepção da saúde comparada',
                descricao: 'Avaliação geral da saúde comparada historicamente.',
                perguntas: [
                    {
                        texto: 'Comparada há um ano atrás, como você se classificaria sua saúde em geral, agora?',
                        tipo: 'radio',
                        opcoes: ['Melhor', 'Pior', 'Igual'],
                        resposta: '',
                        validacao: { required: true }
                    },

                ]
            },
            {
                titulo: 'Atividades de Vida Diária',
                descricao: 'Avaliação de atividades como compras, finanças, e pequenos trabalhos domésticos.',
                perguntas: [
                    {
                        texto: 'Você deixou de fazer compras por causa da sua saúde ou condição física?',
                        tipo: 'radio',
                        opcoes: ["Sim", "Não ou não faz compras por outros motivos que não a saúde"],
                        resposta: '',
                        validacao: { required: true }
                    },
                    {
                        texto: 'Você deixou de controlar seu dinheiro ou os gastos da casa por causa da sua saúde ou condição física?',
                        tipo: 'radio',
                        opcoes: ["Sim", "Não ou não controla o dinheiro por outros motivos que não a saúde"],
                        resposta: '',
                        validacao: { required: true }
                    },
                    {
                        texto: 'Você deixou de realizar pequenos trabalhos domésticos (limpeza leve, arrumar a casa, lavar louças) por causa da sua saúde ou condição física?',
                        tipo: 'radio',
                        opcoes: ["Sim", "Não ou não faz trabalhos domésticos por outros motivos que não a saúde"],
                        resposta: '',
                        validacao: { required: true }
                    },
                    {
                        texto: 'Você deixou de tomar banho sozinho por causa da sua saúde ou condição física?',
                        tipo: 'radio',
                        opcoes: ["Sim", "Não"],
                        resposta: '',
                        validacao: { required: true }
                    }
                ]
            },
            {
                titulo: 'Cognição',
                descricao: 'Avaliação de possíveis sinais de problemas de memória.',
                perguntas: [
                    {
                        texto: 'Algum familiar ou amigo falou que você está ficando esquecido?',
                        tipo: 'radio',
                        opcoes: ["Sim", "Não"],
                        resposta: '',
                        validacao: { required: true }
                    },
                    {
                        texto: 'Este esquecimento está piorando nos últimos meses?',
                        tipo: 'radio',
                        opcoes: ["Sim", "Não"],
                        resposta: '',
                        validacao: { required: true }
                    },
                    {
                        texto: 'Este esquecimento está impedindo a realização de alguma atividade do cotidiano?',
                        tipo: 'radio',
                        opcoes: ["Sim", "Não"],
                        resposta: '',
                        validacao: { required: true }
                    }
                ]
            },
            {
                titulo: 'Humor',
                descricao: 'Avaliação de sintomas relacionados ao humor.',
                perguntas: [
                    {
                        texto: 'No último mês, você ficou com desânimo, tristeza ou desesperança?',
                        tipo: 'radio',
                        opcoes: ["Sim", "Não"],
                        resposta: '',
                        validacao: { required: true }
                    },
                    {
                        texto: 'No último mês, você perdeu o interesse ou prazer em atividades anteriormente prazerosas?',
                        tipo: 'radio',
                        opcoes: ["Sim", "Não"],
                        resposta: '',
                        validacao: { required: true }
                    }
                ]
            },
            {
                titulo: 'Mobilidade',
                descricao: 'Avaliação de mobilidade e capacidade física.',
                perguntas: [
                    {
                        texto: 'Você é incapaz de elevar os braços acima do nível do ombro?',
                        tipo: 'radio',
                        opcoes: ["Sim", "Não"],
                        resposta: '',
                        validacao: { required: true }
                    },
                    {
                        texto: 'Você é incapaz de segurar pequenos objetos?',
                        tipo: 'radio',
                        opcoes: ["Sim", "Não"],
                        resposta: '',
                        validacao: { required: true }
                    },
                    {
                        texto: 'Você tem alguma das quatro condições abaixo relacionadas?',
                        tipo: 'checkbox',
                        opcoes: [
                            "Perda de peso não intencional de 4,5 kg ou 5% do peso corporal no último ano ou 6 kg nos últimos 6 meses ou 3 kg no último mês",
                            // "Índice de Massa Corporal (IMC) menor que 22 kg/m²", -- Calcular isso aqui e atribuir pontuação
                            "Circunferência da panturrilha < 31 cm",
                            "Tempo gasto no teste de velocidade de marcha (4m) > 5 segundos"
                        ],
                        resposta: '',
                        validacao: { required: false }
                    },
                    {
                        texto: 'Você tem dificuldade para caminhar capaz de impedir a realização de alguma atividade do cotidiano?',
                        tipo: 'radio',
                        opcoes: ["Sim", "Não"],
                        resposta: '',
                        validacao: { required: true }
                    },
                    {
                        texto: 'Você caiu no último ano? Quantas vezes?',
                        tipo: 'radio',
                        opcoes: ["Não", "1 vez", "2 vezes", "3 vezes ou mais"],
                        resposta: '',
                        validacao: { required: true }
                    },
                    {
                        texto: 'Você perde urina ou fezes, sem querer, em algum momento?',
                        tipo: 'radio',
                        opcoes: ["Sim", "Não"],
                        resposta: '',
                        validacao: { required: true }
                    },
                ]
            },
            {
                titulo: 'Comunicação',
                descricao: 'Avaliação de problemas de comunicação relacionados à saúde.',
                perguntas: [
                    {
                        texto: 'Você tem problemas de visão capazes de impedir a realização de alguma atividade do cotidiano? É permitido o uso de óculos ou lentes de contato.',
                        tipo: 'radio',
                        opcoes: ["Sim", "Não"],
                        resposta: '',
                        validacao: { required: true }
                    },
                    {
                        texto: 'Você tem problemas de audição que impedem a realização de atividades do cotidiano? É permitido o uso de aparelhos de audição.',
                        tipo: 'radio',
                        opcoes: ["Sim", "Não"],
                        resposta: '',
                        validacao: { required: true }
                    },
                    {
                        texto: 'Você tem alguma das três condições abaixo relacionadas?',
                        tipo: 'checkbox',
                        opcoes: [
                            "Cinco ou mais doenças crônicas",
                            "Uso regular de cinco ou mais medicamentos diferentes",
                            "Internação recente, nos últimos 6 meses"
                        ],
                        resposta: '',
                        validacao: { required: false }
                    }
                ]
            },
            {
              titulo: 'Atividades Adicionais',
              descricao: 'Classificação da fadiga em situações cotidianas.',
              perguntas: [
                  {
                      texto: 'Classifique os níveis de fadiga ao subir escadas por 5 minutos.',
                      tipo: 'range',
                      resposta: '',
                      validacao: { min: 0, max: 5, required: true }
                  },
                  {
                      texto: 'Classifique os níveis de fadiga ao realizar caminhada rápida por 15 minutos.',
                      tipo: 'range',
                      resposta: '',
                      validacao: { min: 0, max: 5, required: true }
                  }
              ]
          },
          {
              titulo: 'Atividades Domésticas',
              descricao: 'Classificação da fadiga durante tarefas domésticas pesadas.',
              perguntas: [
                  {
                      texto: 'Classifique os níveis de fadiga ao carregar compras por 10 minutos.',
                      tipo: 'range',
                      resposta: '',
                      validacao: { min: 0, max: 5, required: true }
                  }
              ]
          }
        ]
    },

    [TipoFormulario.MINIMENTAL]: {
        id: 4,
        tipo: TipoFormulario.MINIMENTAL,
        titulo: 'Mini Exame do Estado Mental (MEEM)',
        descricao: 'Avaliação cognitiva para identificar possíveis déficits cognitivos.',
        etapas: [
            {
                titulo: 'Orientação Temporal',
                descricao: 'Pergunte ao paciente as seguintes questões, será calculado 1 ponto para cada.',
                perguntas: [
                    {
                        texto: 'Que dia é hoje?',
                        tipo: 'radio',
                        opcoes: ["O paciente respondeu corretamente", "Respondeu incorretamente"],
                        resposta: '',
                        validacao: { required: true }
                    },
                    {
                        texto: 'Em que mês estamos?',
                        tipo: 'radio',
                        opcoes: ["O paciente respondeu corretamente", "Respondeu incorretamente"],
                        resposta: '',
                        validacao: { required: true }
                    },
                    {
                        texto: 'Em que ano estamos?',
                        tipo: 'radio',
                        opcoes: ["O paciente respondeu corretamente", "Respondeu incorretamente"],
                        resposta: '',
                        validacao: { required: true }
                    },
                    {
                        texto: 'Em que dia da semana estamos?',
                        tipo: 'radio',
                        opcoes: ["O paciente respondeu corretamente", "Respondeu incorretamente"],
                        resposta: '',
                        validacao: { required: true }
                    },
                    {
                        texto: 'Qual a hora atual aproximada (considere variação de mais ou menos 1 hora)',
                        tipo: 'radio',
                        opcoes: ["O paciente respondeu corretamente", "Respondeu incorretamente"],
                        resposta: '',
                        validacao: { required: true }
                    }
                ]
            },
            {
                titulo: 'Orientação Espacial',
                descricao: 'Pergunte ao paciente as seguintes questões, será calculado 1 ponto para cada.',
                perguntas: [
                    {
                        texto: 'Em que local nós estamos? (consultório, sala, dormitório...)',
                        tipo: 'radio',
                        opcoes: ["O paciente respondeu corretamente", "Respondeu incorretamente"],
                        resposta: '',
                        validacao: { required: true }
                    },
                    {
                        texto: 'Que local é este aqui? (apontando ao redor num sentido mais amplo.... hospital, casa de repouso, própria casa...)',
                        tipo: 'radio',
                        opcoes: ["O paciente respondeu corretamente", "Respondeu incorretamente"],
                        resposta: '',
                        validacao: { required: true }
                    },
                    {
                        texto: 'Em que bairro nós estamos ou qual o nome de uma rua próxima?',
                        tipo: 'radio',
                        opcoes: ["O paciente respondeu corretamente", "Respondeu incorretamente"],
                        resposta: '',
                        validacao: { required: true }
                    },
                    {
                        texto: 'Em que cidade nós estamos?',
                        tipo: 'radio',
                        opcoes: ["O paciente respondeu corretamente", "Respondeu incorretamente"],
                        resposta: '',
                        validacao: { required: true }
                    },
                    {
                        texto: 'Em que estado nós estamos?',
                        tipo: 'radio',
                        opcoes: ["O paciente respondeu corretamente", "Respondeu incorretamente"],
                        resposta: '',
                        validacao: { required: true }
                    }
                ]
            },
            {
                titulo: 'Memória Imediata',
                descricao: 'Eu vou dizer 3 palavras e você irá repeti-las a seguir: CARRO – VASO – TIJOLO. Pode repeti-las até três vezes para o aprendizado, se houver erros.',
                perguntas: [
                    {
                        texto: 'Marque cada palavra repetida corretamente na 1ª vez.',
                        tipo: 'checkbox',
                        opcoes: [
                            "CARRO",
                            "VASO",
                            "TIJOLO"
                        ],
                        resposta: '',
                        validacao: { required: false }
                    }
                ]
            },
            {
                titulo: 'Cálculo',
                descricao: 'Subtração de 7 seriadamente (100-7; 93-7; 86-7; 79-7; 72-7; 65). Se houver erro, corrija-o e prossiga. Considere correto se o avaliado espontaneamente se autocorrigir.',
                perguntas: [
                    {
                        texto: 'Considere 1 ponto para cada resultado correto.',
                        tipo: 'range',
                        resposta: '',
                        validacao: { required: true, min: 0, max: 6 }
                    }
                ]
            },
            {
                titulo: 'Evocação das palavras',
                descricao: 'Pergunte quais as palavras que o indivíduo acabara de repetir (CARRO-VASO-TIJOLO).',
                perguntas: [
                    {
                        texto: 'Marque cada palavra repetida corretamente.',
                        tipo: 'checkbox',
                        opcoes: [
                            "CARRO",
                            "VASO",
                            "TIJOLO"
                        ],
                        resposta: '',
                        validacao: { required: false }
                    }
                ]
            },
            {
                titulo: 'Nomeação',
                descricao: 'Peça para o indivíduo nomear os objetos mostrados (relógio, caneta). ',
                perguntas: [
                    {
                        texto: 'Marque cada objeto nomeado corretamente.',
                        tipo: 'checkbox',
                        opcoes: [
                            "Relógio",
                            "Caneta"
                        ],
                        resposta: '',
                        validacao: { required: false }
                    }
                ]
            },
            {
                titulo: 'Repetição',
                descricao: 'Preste atenção: Vou lhe dizer uma frase e quero que você repita depois de mim: “NEM AQUI, NEM ALI, NEM LÁ”.',
                perguntas: [
                    {
                        texto: 'Considere somente se a repetição for perfeita',
                        tipo: 'radio',
                        opcoes: ["O paciente respondeu corretamente", "Respondeu incorretamente"],
                        resposta: '',
                        validacao: { required: true }
                    }
                ]
            },
            {
                titulo: 'Comando',
                descricao: 'Pegue este papel com a mão direita (1 ponto), dobre-o ao meio (1 ponto) e coloque-o no chão (1 ponto).  Se o indivíduo pedir ajuda no meio da tarefa não dê dicas.',
                perguntas: [
                    {
                        texto: 'Avalie cada critério.',
                        tipo: 'checkbox',
                        opcoes: [
                            "Pegou o papel com a mão direita.",
                            "Dobrou o papel ao meio.",
                            "Colocou o papel no chão."
                        ],
                        resposta: '',
                        validacao: { required: false }
                    }
                ]
            },
            {
                titulo: 'Leitura e Obediência',
                descricao: 'Mostre a frase escrita: “FECHE OS OLHOS” e peça para o indivíduo fazer o que está sendo mandado. Não auxilie se pedir ajuda ou se só ler a frase sem realizar o comando. ',
                perguntas: [
                    {
                        texto: 'Avalie o resultado.',
                        tipo: 'radio',
                        opcoes: ["O paciente respondeu corretamente", "Respondeu incorretamente"],
                        resposta: '',
                        validacao: { required: true }
                    }
                ]
            },
            {
                titulo: 'Escrita',
                descricao: 'Peça ao indivíduo para escrever uma frase. Se não compreender o significado, ajude com: alguma frase que tenha começo, meio e fim, alguma coisa que aconteceu hoje; alguma coisa que queira dizer. Para a correção não são considerados erros gramaticais ou de ortografia. (1 ponto).',
                perguntas: [
                    {
                        texto: 'Avalie o resultado.',
                        tipo: 'radio',
                        opcoes: ["O paciente respondeu corretamente", "Respondeu incorretamente"],
                        resposta: '',
                        validacao: { required: true }
                    }
                ]
            },
            {
                titulo: 'Cópia do Desenho',
                descricao: 'Mostre o modelo e peça para fazer o melhor possível. Considere apenas se houver 2 pentágonos intersecionados (10 ângulos) formando uma figura de quatro lados ou com dois ângulos (1 ponto).',
                perguntas: [
                    {
                        texto: 'Avalie o desenho.',
                        tipo: 'radio',
                        opcoes: ["O paciente respondeu corretamente", "Respondeu incorretamente"],
                        resposta: '',
                        validacao: { required: true }
                    }
                ]
            },
            {
              titulo: 'Cálculo Adicional',
              descricao: 'Avaliação de habilidades matemáticas simples.',
              perguntas: [
                  {
                      texto: 'Conte de 1 a 10 de trás para frente. Avalie se ele erra.',
                      tipo: 'radio',
                      opcoes: ['Sim', 'Não'],
                      resposta: '',
                      validacao: { required: true }
                  }
              ]
          },
          {
              titulo: 'Desenho Adicional',
              descricao: 'Avaliação da capacidade de reprodução gráfica.',
              perguntas: [
                  {
                      texto: 'Desenhe um relógio marcando 3 horas. Avalie se os ponteiros e números estão corretos.',
                      tipo: 'radio',
                      opcoes: ['Sim', 'Não'],
                      resposta: '',
                      validacao: { required: true }
                  }
              ]
          }
        ]
    },

    [TipoFormulario.FACTF]: {
        id: 5,
        tipo: TipoFormulario.FACTF,
        titulo: 'Functional Assessment of Cancer Therapy - Fatigue (FACT-F)',
        descricao: 'Avaliação da qualidade de vida e fadiga em pacientes com câncer',
        etapas: [
            {
                titulo: 'Bem-estar Físico',
                descricao: 'Avaliação do seu estado físico durante os últimos 7 dias',
                perguntas: [
                    { texto: 'Estou sem energia', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Fico enjoado(a)', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Por causa do meu estado físico, tenho dificuldade em atender às necessidades da minha família', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Tenho dores', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Sinto-me incomodado(a) pelos efeitos secundários do tratamento', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Sinto-me doente', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Tenho que me deitar durante o dia', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } }
                ]
            },
            {
                titulo: 'Bem-estar Social/Familiar',
                descricao: 'Avaliação do seu relacionamento com família e amigos durante os últimos 7 dias',
                perguntas: [
                    { texto: 'Sinto que tenho uma boa relação com os meus amigos', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Recebo apoio emocional da minha família', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Recebo apoio dos meus amigos', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'A minha família aceita a minha doença', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Estou satisfeito(a) com a maneira como a minha família fala sobre a minha doença', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Sinto-me próximo(a) do(a) meu (minha) parceiro(a) (ou da pessoa que me dá maior apoio)', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Estou satisfeito(a) com a minha vida sexual', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: false } }
                ]
            },
            {
                titulo: 'Bem-estar Emocional',
                descricao: 'Avaliação do seu estado emocional durante os últimos 7 dias',
                perguntas: [
                    { texto: 'Sinto-me triste', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Estou satisfeito(a) com a maneira como enfrento a minha doença', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Estou perdendo a esperança na luta contra a minha doença', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Sinto-me nervoso(a)', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Estou preocupado(a) com a idéia de morrer', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Estou preocupado(a) que o meu estado venha a piorar', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } }
                ]
            },
            {
                titulo: 'Bem-estar Funcional',
                descricao: 'Avaliação da sua capacidade funcional durante os últimos 7 dias',
                perguntas: [
                    { texto: 'Sou capaz de trabalhar (inclusive em casa)', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Sinto-me realizado(a) com o meu trabalho (inclusive em casa)', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Sou capaz de sentir prazer em viver', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Aceito a minha doença', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Durmo bem', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Gosto das coisas que normalmente faço para me divertir', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Estou satisfeito(a) com a qualidade da minha vida neste momento', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } }
                ]
            },
            {
                titulo: 'Preocupações Adicionais',
                descricao: 'Avaliação de preocupações específicas durante os últimos 7 dias',
                perguntas: [
                    { texto: 'Sinto-me fatigado(a)', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Sinto fraqueza generalizada', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Sinto-me sem forças', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Sinto-me cansado(a)', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Tenho dificuldade em começar as coisas porque estou cansado(a)', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Tenho dificuldade em acabar as coisas porque estou cansado(a)', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Tenho energia', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Sou capaz de fazer as minhas atividades normais', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Preciso (de) dormir durante o dia', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Estou cansado(a) demais para comer', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Preciso de ajuda para fazer as minhas atividades normais', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Estou frustrado(a) por estar cansado(a) demais para fazer as coisas que quero', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Tenho que limitar as minhas atividades sociais por estar cansado(a)', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } }
                ]
            },
            {
              titulo: 'Impacto no Dia a Dia',
              descricao: 'Avaliação de como a fadiga impacta o cotidiano.',
              perguntas: [
                  {
                      texto: 'Tenho que planejar meu dia para evitar o cansaço.',
                      tipo: 'radio',
                      resposta: '',
                      opcoes: ['Sim', 'Não'],
                      validacao: { required: true }
                  },
                  {
                      texto: 'Evito sair de casa por falta de energia.',
                      tipo: 'radio',
                      resposta: '',
                      opcoes: ['Sim', 'Não'],
                      validacao: { required: true }
                  }
              ]
          }
        ]
    }
};
