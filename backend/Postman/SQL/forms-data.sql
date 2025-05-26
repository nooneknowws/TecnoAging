--
-- PostgreSQL database dump
--

-- Dumped from database version 17.4 (Debian 17.4-1.pgdg120+2)
-- Dumped by pg_dump version 17.4 (Debian 17.4-1.pgdg120+2)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Data for Name: formulario; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.formulario VALUES (1, 'Avaliação do nível de fadiga física e mental após diferentes atividades', 'pfs', 'Formulário PFS');
INSERT INTO public.formulario VALUES (2, 'Avaliação cognitiva para identificar possíveis déficits cognitivos.', 'minimental', 'Mini Exame do Estado Mental (MEEM)');
INSERT INTO public.formulario VALUES (3, 'Avaliação multidimensional do grau de vulnerabilidade clínica e funcional de idosos', 'ivcf20', 'Índice de Vulnerabilidade Clínico-Funcional (IVCF-20)');
INSERT INTO public.formulario VALUES (4, 'Nível de Atividade Física e Comportamento Sedentário', 'sedentarismo', 'Nível de Atividade Física e Comportamento Sedentário');
INSERT INTO public.formulario VALUES (5, 'Avaliação da qualidade de vida e fadiga em pacientes com câncer', 'factf', 'Functional Assessment of Cancer Therapy - Fatigue (FACT-F)');


--
-- Data for Name: etapa; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.etapa VALUES (1, 'Instruções: Nas perguntas a seguir indique o nível de fadiga física e mental (ou seja, cansaço, exaustão) que você espera ou imagina sentir imediatamente após completar cada uma das dez atividades listadas. Para cada atividade (a-j), circule as respostas para fadiga física e mental entre 0 e 5, onde ''0'' é igual a nenhuma fadiga e ''5'' é igual a fadiga extrema.', 'Escala de Fatigabilidade de Pittsburgh', 1);
INSERT INTO public.etapa VALUES (2, 'Classifique os níveis de fadiga ao caminhar à lazer por 30 minutos.', 'Atividade 1', 1);
INSERT INTO public.etapa VALUES (3, 'Classifique os níveis de fadiga: Atividade doméstica leve por 1 hora (limpar, cozinhar, tirar o pó, assar, arrumar camas, lavar louça, regar plantas)', 'Atividade 2', 1);
INSERT INTO public.etapa VALUES (4, 'Classifique os níveis de fadiga: Participar de atividade social por 1 hora (festa, jantar, centro de idosos, reunião com família/amigos, jogar cartas)', 'Atividade 3', 1);
INSERT INTO public.etapa VALUES (5, 'Classifique os níveis de fadiga: Atividade de alta intensidade por 30 minutos (corrida, caminhada, ciclismo, natação, esportes com raquete, aparelhos aeróbicos, dança, zumba)', 'Atividade 4', 1);
INSERT INTO public.etapa VALUES (6, 'Pergunte ao paciente as seguintes questões, será calculado 1 ponto para cada.', 'Orientação Temporal', 2);
INSERT INTO public.etapa VALUES (7, 'Pergunte ao paciente as seguintes questões, será calculado 1 ponto para cada.', 'Orientação Espacial', 2);
INSERT INTO public.etapa VALUES (8, 'Eu vou dizer 3 palavras e você irá repeti-las a seguir: CARRO – VASO – TIJOLO. Pode repeti-las até três vezes para o aprendizado, se houver erros.', 'Memória Imediata', 2);
INSERT INTO public.etapa VALUES (9, 'Subtração de 7 seriadamente (100-7; 93-7; 86-7; 79-7; 72-7; 65). Se houver erro, corrija-o e prossiga. Considere correto se o avaliado espontaneamente se autocorrigir.', 'Cálculo', 2);
INSERT INTO public.etapa VALUES (10, 'Pergunte quais as palavras que o indivíduo acabara de repetir (CARRO-VASO-TIJOLO).', 'Evocação das palavras', 2);
INSERT INTO public.etapa VALUES (11, 'Peça para o indivíduo nomear os objetos mostrados (relógio, caneta).', 'Nomeação', 2);
INSERT INTO public.etapa VALUES (12, 'Preste atenção: Vou lhe dizer uma frase e quero que você repita depois de mim: “NEM AQUI, NEM ALI, NEM LÁ”.', 'Repetição', 2);
INSERT INTO public.etapa VALUES (13, 'Pegue este papel com a mão direita (1 ponto), dobre-o ao meio (1 ponto) e coloque-o no chão (1 ponto).  Se o indivíduo pedir ajuda no meio da tarefa não dê dicas.', 'Comando', 2);
INSERT INTO public.etapa VALUES (14, 'Mostre a frase escrita: “FECHE OS OLHOS” e peça para o indivíduo fazer o que está sendo mandado. Não auxilie se pedir ajuda ou se só ler a frase sem realizar o comando.', 'Leitura e Obediência', 2);
INSERT INTO public.etapa VALUES (15, 'Peça ao indivíduo para escrever uma frase. Se não compreender o significado, ajude com: alguma frase que tenha começo, meio e fim, alguma coisa que aconteceu hoje; alguma coisa que queira dizer. Para a correção não são considerados erros gramaticais ou de ortografia. (1 ponto).', 'Escrita', 2);
INSERT INTO public.etapa VALUES (16, 'Mostre o modelo e peça para fazer o melhor possível. Considere apenas se houver 2 pentágonos intersecionados (10 ângulos) formando uma figura de quatro lados ou com dois ângulos (1 ponto).', 'Cópia do Desenho', 2);
INSERT INTO public.etapa VALUES (17, 'Avaliação geral da saúde comparada a outras pessoas da mesma idade.', 'Auto-percepção da Saúde', 3);
INSERT INTO public.etapa VALUES (18, 'Avaliação geral da saúde comparada historicamente.', 'Percepção da saúde comparada', 3);
INSERT INTO public.etapa VALUES (19, 'Avaliação de atividades como compras, finanças, e pequenos trabalhos domésticos.', 'Atividades de Vida Diária', 3);
INSERT INTO public.etapa VALUES (20, 'Avaliação de possíveis sinais de problemas de memória.', 'Cognição', 3);
INSERT INTO public.etapa VALUES (21, 'Avaliação de sintomas relacionados ao humor.', 'Humor', 3);
INSERT INTO public.etapa VALUES (22, 'Avaliação de mobilidade e capacidade física.', 'Mobilidade', 3);
INSERT INTO public.etapa VALUES (23, 'Avaliação de problemas de comunicação relacionados à saúde.', 'Comunicação', 3);
INSERT INTO public.etapa VALUES (24, 'Você consegue realizá-la conversando com dificuldade enquanto se movimenta e não vai conseguir cantar.', 'Atividade Moderada', 4);
INSERT INTO public.etapa VALUES (25, 'Você não vai conseguir nem conversar. A sua respiração vai ser muito mais rápida que o normal e os batimentos do seu coração vão aumentar muito.', 'Atividade Vigorosa', 4);
INSERT INTO public.etapa VALUES (26, 'Quanto tempo do seu dia, enquanto você está acordado, você gasta sentado, reclinado ou deitado, assistindo televisão, no celular, em frente ao computador, realizando trabalhos manuais, dirigindo ou lendo?', 'Comportamento Sedentário', 4);
INSERT INTO public.etapa VALUES (27, 'Avaliação do seu estado físico durante os últimos 7 dias', 'Bem-estar Físico', 5);
INSERT INTO public.etapa VALUES (28, 'Avaliação do seu relacionamento com família e amigos durante os últimos 7 dias', 'Bem-estar Social/Familiar', 5);
INSERT INTO public.etapa VALUES (29, 'Avaliação do seu estado emocional durante os últimos 7 dias', 'Bem-estar Emocional', 5);
INSERT INTO public.etapa VALUES (30, 'Avaliação da sua capacidade funcional durante os últimos 7 dias', 'Bem-estar Funcional', 5);
INSERT INTO public.etapa VALUES (31, 'Avaliação de preocupações específicas durante os últimos 7 dias', 'Preocupações Adicionais', 5);


--
-- Data for Name: pergunta; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.pergunta VALUES (1, 'Fadiga Física', 'range', 5, 0, true, 2);
INSERT INTO public.pergunta VALUES (2, 'Fadiga Mental', 'range', 5, 0, true, 2);
INSERT INTO public.pergunta VALUES (3, 'Fadiga Física', 'range', 5, 0, true, 3);
INSERT INTO public.pergunta VALUES (4, 'Fadiga Mental', 'range', 5, 0, true, 3);
INSERT INTO public.pergunta VALUES (5, 'Fadiga Física', 'range', 5, 0, true, 4);
INSERT INTO public.pergunta VALUES (6, 'Fadiga Mental', 'range', 5, 0, true, 4);
INSERT INTO public.pergunta VALUES (7, 'Fadiga Física', 'range', 5, 0, true, 5);
INSERT INTO public.pergunta VALUES (8, 'Fadiga Mental', 'range', 5, 0, true, 5);
INSERT INTO public.pergunta VALUES (9, 'Que dia é hoje?', 'radio', NULL, NULL, true, 6);
INSERT INTO public.pergunta VALUES (10, 'Em que mês estamos?', 'radio', NULL, NULL, true, 6);
INSERT INTO public.pergunta VALUES (11, 'Em que ano estamos?', 'radio', NULL, NULL, true, 6);
INSERT INTO public.pergunta VALUES (12, 'Em que dia da semana estamos?', 'radio', NULL, NULL, true, 6);
INSERT INTO public.pergunta VALUES (13, 'Qual a hora atual aproximada (considere variação de mais ou menos 1 hora)', 'radio', NULL, NULL, true, 6);
INSERT INTO public.pergunta VALUES (14, 'Em que local nós estamos? (consultório, sala, dormitório...)', 'radio', NULL, NULL, true, 7);
INSERT INTO public.pergunta VALUES (15, 'Que local é este aqui? (apontando ao redor num sentido mais amplo.... hospital, casa de repouso, própria casa...)', 'radio', NULL, NULL, true, 7);
INSERT INTO public.pergunta VALUES (16, 'Em que bairro nós estamos ou qual o nome de uma rua próxima?', 'radio', NULL, NULL, true, 7);
INSERT INTO public.pergunta VALUES (17, 'Em que cidade nós estamos?', 'radio', NULL, NULL, true, 7);
INSERT INTO public.pergunta VALUES (18, 'Em que estado nós estamos?', 'radio', NULL, NULL, true, 7);
INSERT INTO public.pergunta VALUES (19, 'Marque cada palavra repetida corretamente na 1ª vez.', 'checkbox', NULL, NULL, false, 8);
INSERT INTO public.pergunta VALUES (20, 'Considere 1 ponto para cada resultado correto.', 'range', 6, 0, true, 9);
INSERT INTO public.pergunta VALUES (21, 'Marque cada palavra repetida corretamente.', 'checkbox', NULL, NULL, false, 10);
INSERT INTO public.pergunta VALUES (22, 'Marque cada objeto nomeado corretamente.', 'checkbox', NULL, NULL, false, 11);
INSERT INTO public.pergunta VALUES (23, 'Considere somente se a repetição for perfeita', 'radio', NULL, NULL, true, 12);
INSERT INTO public.pergunta VALUES (24, 'Avalie cada critério.', 'checkbox', NULL, NULL, false, 13);
INSERT INTO public.pergunta VALUES (25, 'Avalie o resultado.', 'radio', NULL, NULL, true, 14);
INSERT INTO public.pergunta VALUES (26, 'Avalie o resultado.', 'radio', NULL, NULL, true, 15);
INSERT INTO public.pergunta VALUES (27, 'Avalie o desenho.', 'radio', NULL, NULL, true, 16);
INSERT INTO public.pergunta VALUES (28, 'Em geral, comparado com outras pessoas da sua idade, como você diria que está a sua saúde?', 'radio', NULL, NULL, NULL, 17);
INSERT INTO public.pergunta VALUES (29, 'Comparada há um ano atrás, como você se classificaria sua saúde em geral, agora?', 'radio', NULL, NULL, NULL, 18);
INSERT INTO public.pergunta VALUES (30, 'Você deixou de fazer compras por causa da sua saúde ou condição física?', 'radio', NULL, NULL, NULL, 19);
INSERT INTO public.pergunta VALUES (31, 'Você deixou de controlar seu dinheiro ou os gastos da casa por causa da sua saúde ou condição física?', 'radio', NULL, NULL, NULL, 19);
INSERT INTO public.pergunta VALUES (32, 'Você deixou de realizar pequenos trabalhos domésticos (limpeza leve, arrumar a casa, lavar louças) por causa da sua saúde ou condição física?', 'radio', NULL, NULL, NULL, 19);
INSERT INTO public.pergunta VALUES (33, 'Você deixou de tomar banho sozinho por causa da sua saúde ou condição física?', 'radio', NULL, NULL, NULL, 19);
INSERT INTO public.pergunta VALUES (34, 'Algum familiar ou amigo falou que você está ficando esquecido?', 'radio', NULL, NULL, NULL, 20);
INSERT INTO public.pergunta VALUES (35, 'Este esquecimento está piorando nos últimos meses?', 'radio', NULL, NULL, NULL, 20);
INSERT INTO public.pergunta VALUES (36, 'Este esquecimento está impedindo a realização de alguma atividade do cotidiano?', 'radio', NULL, NULL, NULL, 20);
INSERT INTO public.pergunta VALUES (37, 'No último mês, você ficou com desânimo, tristeza ou desesperança?', 'radio', NULL, NULL, NULL, 21);
INSERT INTO public.pergunta VALUES (38, 'No último mês, você perdeu o interesse ou prazer em atividades anteriormente prazerosas?', 'radio', NULL, NULL, NULL, 21);
INSERT INTO public.pergunta VALUES (39, 'Você é incapaz de elevar os braços acima do nível do ombro?', 'radio', NULL, NULL, NULL, 22);
INSERT INTO public.pergunta VALUES (40, 'Você é incapaz de segurar pequenos objetos?', 'radio', NULL, NULL, NULL, 22);
INSERT INTO public.pergunta VALUES (41, 'Você tem alguma das quatro condições abaixo relacionadas?', 'checkbox', NULL, NULL, NULL, 22);
INSERT INTO public.pergunta VALUES (42, 'Você tem dificuldade para caminhar capaz de impedir a realização de alguma atividade do cotidiano?', 'radio', NULL, NULL, NULL, 22);
INSERT INTO public.pergunta VALUES (43, 'Você caiu no último ano? Quantas vezes?', 'radio', NULL, NULL, NULL, 22);
INSERT INTO public.pergunta VALUES (44, 'Você perde urina ou fezes, sem querer, em algum momento?', 'radio', NULL, NULL, NULL, 22);
INSERT INTO public.pergunta VALUES (45, 'Você tem problemas de visão capazes de impedir a realização de alguma atividade do cotidiano? É permitido o uso de óculos ou lentes de contato.', 'radio', NULL, NULL, NULL, 23);
INSERT INTO public.pergunta VALUES (46, 'Você tem problemas de audição que impedem a realização de atividades do cotidiano? É permitido o uso de aparelhos de audição.', 'radio', NULL, NULL, NULL, 23);
INSERT INTO public.pergunta VALUES (47, 'Você tem alguma das três condições abaixo relacionadas?', 'checkbox', NULL, NULL, NULL, 23);
INSERT INTO public.pergunta VALUES (48, 'Quanto tempo por dia você realiza atividades moderadas? (HH:MM)', 'tempo', 7, 0, false, 24);
INSERT INTO public.pergunta VALUES (49, 'Quantos dias por semana?', 'numero', 7, 0, false, 24);
INSERT INTO public.pergunta VALUES (50, 'Quanto tempo por dia você realiza atividades vigorosas? (HH:MM)', 'tempo', NULL, NULL, NULL, 25);
INSERT INTO public.pergunta VALUES (51, 'Quantos dias por semana?', 'numero', 7, 0, false, 25);
INSERT INTO public.pergunta VALUES (52, 'Quanto tempo por dia? (HH:MM)', 'tempo', 7, 0, false, 26);
INSERT INTO public.pergunta VALUES (53, 'Quantos dias por semana?', 'numero', 7, 0, false, 26);
INSERT INTO public.pergunta VALUES (54, 'Estou sem energia', 'radio', NULL, NULL, true, 27);
INSERT INTO public.pergunta VALUES (55, 'Fico enjoado(a)', 'radio', NULL, NULL, true, 27);
INSERT INTO public.pergunta VALUES (56, 'Por causa do meu estado físico, tenho dificuldade em atender às necessidades da minha família', 'radio', NULL, NULL, true, 27);
INSERT INTO public.pergunta VALUES (57, 'Tenho dores', 'radio', NULL, NULL, true, 27);
INSERT INTO public.pergunta VALUES (58, 'Sinto-me incomodado(a) pelos efeitos secundários do tratamento', 'radio', NULL, NULL, true, 27);
INSERT INTO public.pergunta VALUES (59, 'Sinto-me doente', 'radio', NULL, NULL, true, 27);
INSERT INTO public.pergunta VALUES (60, 'Tenho que me deitar durante o dia', 'radio', NULL, NULL, true, 27);
INSERT INTO public.pergunta VALUES (61, 'Sinto que tenho uma boa relação com os meus amigos', 'radio', NULL, NULL, true, 28);
INSERT INTO public.pergunta VALUES (62, 'Recebo apoio emocional da minha família', 'radio', NULL, NULL, true, 28);
INSERT INTO public.pergunta VALUES (63, 'Recebo apoio dos meus amigos', 'radio', NULL, NULL, true, 28);
INSERT INTO public.pergunta VALUES (64, 'A minha família aceita a minha doença', 'radio', NULL, NULL, true, 28);
INSERT INTO public.pergunta VALUES (65, 'Estou satisfeito(a) com a maneira como a minha família fala sobre a minha doença', 'radio', NULL, NULL, true, 28);
INSERT INTO public.pergunta VALUES (66, 'Sinto-me próximo(a) do(a) meu (minha) parceiro(a) (ou da pessoa que me dá maior apoio)', 'radio', NULL, NULL, true, 28);
INSERT INTO public.pergunta VALUES (67, 'Estou satisfeito(a) com a minha vida sexual', 'radio', NULL, NULL, false, 28);
INSERT INTO public.pergunta VALUES (68, 'Sinto-me triste', 'radio', NULL, NULL, true, 29);
INSERT INTO public.pergunta VALUES (69, 'Estou satisfeito(a) com a maneira como enfrento a minha doença', 'radio', NULL, NULL, true, 29);
INSERT INTO public.pergunta VALUES (70, 'Estou perdendo a esperança na luta contra a minha doença', 'radio', NULL, NULL, true, 29);
INSERT INTO public.pergunta VALUES (71, 'Sinto-me nervoso(a)', 'radio', NULL, NULL, true, 29);
INSERT INTO public.pergunta VALUES (72, 'Estou preocupado(a) com a idéia de morrer', 'radio', NULL, NULL, true, 29);
INSERT INTO public.pergunta VALUES (73, 'Estou preocupado(a) que o meu estado venha a piorar', 'radio', NULL, NULL, true, 29);
INSERT INTO public.pergunta VALUES (74, 'Sou capaz de trabalhar (inclusive em casa)', 'radio', NULL, NULL, true, 30);
INSERT INTO public.pergunta VALUES (75, 'Sinto-me realizado(a) com o meu trabalho (inclusive em casa)', 'radio', NULL, NULL, true, 30);
INSERT INTO public.pergunta VALUES (76, 'Sou capaz de sentir prazer em viver', 'radio', NULL, NULL, true, 30);
INSERT INTO public.pergunta VALUES (77, 'Aceito a minha doença', 'radio', NULL, NULL, true, 30);
INSERT INTO public.pergunta VALUES (78, 'Durmo bem', 'radio', NULL, NULL, true, 30);
INSERT INTO public.pergunta VALUES (79, 'Gosto das coisas que normalmente faço para me divertir', 'radio', NULL, NULL, true, 30);
INSERT INTO public.pergunta VALUES (80, 'Estou satisfeito(a) com a qualidade da minha vida neste momento', 'radio', NULL, NULL, true, 30);
INSERT INTO public.pergunta VALUES (81, 'Sinto-me fatigado(a)', 'radio', NULL, NULL, true, 31);
INSERT INTO public.pergunta VALUES (82, 'Sinto fraqueza generalizada', 'radio', NULL, NULL, true, 31);
INSERT INTO public.pergunta VALUES (83, 'Sinto-me sem forças', 'radio', NULL, NULL, true, 31);
INSERT INTO public.pergunta VALUES (84, 'Sinto-me cansado(a)', 'radio', NULL, NULL, true, 31);
INSERT INTO public.pergunta VALUES (85, 'Tenho dificuldade em começar as coisas porque estou cansado(a)', 'radio', NULL, NULL, true, 31);
INSERT INTO public.pergunta VALUES (86, 'Tenho dificuldade em acabar as coisas porque estou cansado(a)', 'radio', NULL, NULL, true, 31);
INSERT INTO public.pergunta VALUES (87, 'Tenho energia', 'radio', NULL, NULL, true, 31);
INSERT INTO public.pergunta VALUES (88, 'Sou capaz de fazer as minhas atividades normais', 'radio', NULL, NULL, true, 31);
INSERT INTO public.pergunta VALUES (89, 'Preciso (de) dormir durante o dia', 'radio', NULL, NULL, true, 31);
INSERT INTO public.pergunta VALUES (90, 'Estou cansado(a) demais para comer', 'radio', NULL, NULL, true, 31);
INSERT INTO public.pergunta VALUES (91, 'Preciso de ajuda para fazer as minhas atividades normais', 'radio', NULL, NULL, true, 31);
INSERT INTO public.pergunta VALUES (92, 'Estou frustrado(a) por estar cansado(a) demais para fazer as coisas que quero', 'radio', NULL, NULL, true, 31);
INSERT INTO public.pergunta VALUES (93, 'Tenho que limitar as minhas atividades sociais por estar cansado(a)', 'radio', NULL, NULL, true, 31);


--
-- Name: etapa_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.etapa_id_seq', 31, true);


--
-- Name: formulario_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.formulario_id_seq', 5, true);


--
-- Name: pergunta_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.pergunta_id_seq', 93, true);


--
-- PostgreSQL database dump complete
--

