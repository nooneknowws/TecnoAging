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
-- Name: db-pacientes; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE "db-pacientes" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'en_US.utf8';


ALTER DATABASE "db-pacientes" OWNER TO postgres;

\encoding SQL_ASCII
\connect -reuse-previous=on "dbname='db-pacientes'"

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
-- Data for Name: paciente; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.paciente (id, cpf, data_nasc, bairro, cep, complemento, logradouro, municipio, numero, uf, idade, nome, password_hash, salt, senha, sexo, telefone, altura, cor_raca, data_expedicao, escolaridade, estado_civil, imc, municipio_nasc, nacionalidade, orgao_emissor, peso, rg, socioeconomico, uf_emissor, uf_nasc, version) FROM stdin;
1	45678901234	1978-09-30	Consolação	01305-000	Sala 501	Rua Augusta	São Paulo	1500	SP	46	Ricardo Almeida Costa	DHIOwIQV+Z/SoXpbV8Ss2j28YkXl18RRE1xOGH74gPg=	s9VcaQlGtUlDPDCdhoX6qA==	senha123	M	(11) 96543-2109	1.78	Pardo	1998-12-05	Pós-graduação	Casado	25.628078	Rio de Janeiro	Brasileiro	SSP-RJ	81.2	45.678.901-2	A	RJ	RJ	0
2	78901234567	1965-08-22	Consolação	01301-000	Fundos	Rua da Consolação	São Paulo	100	SP	59	Lucia Ferreira Gomes	2n+Du8P0VMrj5/og/DtkTrRCe3xv6BFKG2AxpujCycM=	9gYQV9ikNnKkSEkkh9z1qQ==	password	F	(11) 96543-2109	1.6	Parda	1985-05-10	Analfabeto	Viúva	21.75781	São Paulo	Brasileira	SSP-SP	55.7	78.901.234-5	E	SP	SP	0
3	90123456789	1975-06-28	Liberdade	01504-000	Apto 303	Rua Vergueiro	São Paulo	1000	SP	49	Patricia Alves Souza	HqXPO78ucXA7H4zeknTQdXVjP1zEOP/CV1mjr22D+Gw=	6cE+BnHc8Jh/oEvke712Fg==	senha456	F	(11) 97654-3210	1.67	Parda	1995-04-15	Fundamental Completo	Casada	22.51784	São Paulo	Brasileira	SSP-SP	62.8	90.123.456-7	D	SP	SP	0
4	12345098765	1980-09-12	Glória	20241-000	Casa 1	Rua da Glória	Rio de Janeiro	200	RJ	44	Juliana Santos Rocha	S3md+0ideBfkxKx03Fb+R3vHo6JNk1OG1Hp/+h0jVKI=	vfFPa1wt0xdxbqhkCRl9gg==	1234abcd	F	(21) 98765-4321	1.62	Parda	2000-03-18	Fundamental Incompleto	Casada	22.443226	Niterói	Brasileira	SSP-RJ	58.9	12.345.098-7	E	RJ	RJ	0
5	45678321098	1988-07-18	Ipanema	22411-000	Apto 302	Rua Barão da Torre	Rio de Janeiro	500	RJ	36	Amanda Costa Pereira	eRrkU4+uYQdrlZycEbDcOelHVHa7NQN16eWS2cKleTQ=	kdOflc4fRizOOuJJQBLHZg==	senha789	F	(21) 97654-3210	1.64	Parda	2008-05-20	Fundamental Completo	Casada	23.572279	Niterói	Brasileira	SSP-RJ	63.4	45.678.321-0	D	RJ	RJ	0
6	89012765432	1983-12-05	Ipanema	22420-000	Apto 401	Rua Prudente de Morais	Rio de Janeiro	900	RJ	41	Isabela Ferreira Alves	swuEuYJT09EDzQqwu9Jl4hH+wbHk1Ktipn0WIHJSb5M=	GETRE/pPniU0bwzg5sldvg==	qwer1234	F	(21) 97654-3210	1.63	Branca	2003-04-18	Fundamental Completo	Casada	21.90523	Rio de Janeiro	Brasileira	SSP-RJ	58.2	89.012.765-4	D	RJ	RJ	0
7	12345678901	1980-07-15	Jardim Paulista	01415-000	Casa 2	Rua das Flores	São Paulo	45	SP	44	Maria Oliveira Santos	B0f6XY6NJtQdQQmogXd5bqiQObUbcpxGMbnSMPSU3Zk=	nURwZbNI69iDZlGQ54pWLw==	abc123	F	(11) 98765-4321	1.68	Branca	2000-05-20	Fundamental Completo	Casada	25.687359	Campinas	Brasileira	SSP-SP	72.5	12.345.678-9	D	SP	SP	0
8	23456789012	1992-11-25	Bela Vista	01310-100	Apto 1501	Avenida Paulista	São Paulo	900	SP	32	Carlos Eduardo Pereira	9GWQU1opbQynvyOh/0j3fNQfVQTqdkKtFiVoh5+qS0c=	9ddadIkyC9G9JgRV4S3Vtw==	pass123	M	(11) 99876-5432	1.72	Pardo	2010-08-15	Superior Completo	Casado	23.086805	Santos	Brasileiro	SSP-SP	68.3	23.456.789-0	B2	SP	SP	0
10	56789012345	1985-02-18	Consolação	01307-000	Apto 201	Rua Frei Caneca	São Paulo	500	SP	40	Fernanda Lima Rodrigues	zxED8EuNp3f9VzaulwQ1OjW8WVLS1dXLWO8l3vF0KeI=	NyeWMpIv7uLOMiscC9PFkw==	123456	F	(11) 97654-3210	1.63	Negra	2005-07-20	Fundamental Incompleto	Casada	23.900034	Osasco	Brasileira	SSP-SP	63.5	56.789.012-3	D	SP	SP	0
11	67890123456	1990-12-05	Pinheiros	01451-000	Apto 1001	Avenida Brigadeiro Faria Lima	São Paulo	2000	SP	34	Marcos Vinicius Oliveira	sliDVILliFRWuAWHA83WIw6lS7YRwFDqNv8XJv/udus=	4FsVgavNBIBLBmstINuGfQ==	teste123	M	(11) 98765-4321	1.82	Branco	2012-09-15	Superior Incompleto	Solteiro	27.200819	Campinas	Brasileiro	SSP-SP	90.1	67.890.123-4	B1	SP	SP	0
14	01234567890	1972-01-10	Pinheiros	05402-000	Apto 801	Avenida Rebouças	São Paulo	600	SP	53	Roberto Carlos Mendes	Mrxw5u82/z2SsGoI3s6J/M9jcd802qf4v4ZpUbzX8V0=	W2YqXMds+x3TVHU/cOxWlw==	abcd1234	M	(11) 98765-4321	1.8	Branco	1992-07-25	Superior Completo	Casado	25.771606	São Paulo	Brasileiro	SSP-SP	83.5	01.234.567-8	B2	SP	SP	0
16	23456109876	1993-04-25	Catete	22220-000	Apto 402	Rua do Catete	Rio de Janeiro	300	RJ	32	Felipe Oliveira Costa	VQp1i6HBHI8VsnY2sIeQH+Osur+BNpAEk/j7Y2OlZIQ=	tDr3tytzh7ny7kVo3Fpunw==	teste456	M	(21) 97654-3210	1.7	Branco	2011-08-12	Médio Completo	Solteiro	24.290655	Rio de Janeiro	Brasileiro	SSP-RJ	70.2	23.456.109-8	C1	RJ	RJ	0
18	56789432109	1980-02-14	Ipanema	22410-000	Apto 901	Rua Visconde de Pirajá	Rio de Janeiro	600	RJ	45	Marcelo Souza Lima	SNX19Ty2508m/Z7QCBxB5fLpXprheNgh3cw+KygmyGA=	+QmOZOmdBsF3bbYveWAPZQ==	1234teste	M	(21) 98765-4321	1.83	Branco	2000-10-15	Superior Completo	Casado	26.366867	Rio de Janeiro	Brasileiro	SSP-RJ	88.3	56.789.432-1	B1	RJ	RJ	0
19	67890543210	1992-05-22	Ipanema	22420-000	Apto 601	Rua Garcia D'Ávila	Rio de Janeiro	700	RJ	32	Carolina Martins Santos	elXQSOuthonvuovapPKg6wcEVTT1NGICKWfXy3EYScs=	BeW+UqBO91zFb6XaF8/d4g==	abcd4567	F	(21) 97654-3210	1.66	Branca	2010-09-12	Médio Completo	Casada	21.810131	Rio de Janeiro	Brasileira	SSP-RJ	60.1	67.890.543-2	C2	RJ	RJ	0
20	78901654321	1987-08-30	Ipanema	22421-000	Apto 701	Rua Nascimento Silva	Rio de Janeiro	800	RJ	37	Lucas Oliveira Costa	ds9MNjGKHHYkkYjKXcE3qh+Z3iK7bc8CCKX3wliyRBI=	uRDod9GypNv7UHRR1yWKOw==	1234qwer	M	(21) 98765-4321	1.74	Pardo	2007-11-20	Superior Incompleto	Casado	24.970272	Rio de Janeiro	Brasileiro	SSP-RJ	75.6	78.901.654-3	B2	RJ	RJ	0
28	89012345678	1988-03-15	Cerqueira César	01
414-000	Apto 502	Rua Haddock Lobo	São Paulo	300	SP	37	Gustavo Henrique Martins	eyppK/x1xW/NWgVKo5tb7kZ42dZrq48WE8HCDvfaR9o=	vPqPRWlrzEOt9yK2orluYA==	123abc	M	(11) 98765-4321	1.75	Branco	2008-11-20	Médio Completo	Casado	24.914288	São Paulo	Brasileiro	SSP-SP	76.3	89.012.345-6	C2	SP	SP	0
33	34567210987	1985-11-30	Ipanema	22420-000	Apto 1201	Avenida Vieira Souto	Rio de Janeiro	400	RJ	39	Rodrigo Silva Almeida	XT29PLhMJxToZipkS/vo0DsmeEQct4l864dC/Gqn5o8=	KVoLtR02+OwbAQteqCgDjw==	password123	M	(21) 98765-4321	1.85	Branco	2005-12-10	Pós-graduação	Casado	27.962013	Rio de Janeiro	Brasileiro	SSP-RJ	95.7	34.567.210-9	A	RJ	RJ	0
56	90123876543	1975-06-20	Ipanema	22410-000	Apto 1101	Rua Aníbal de Mendonça	Rio de Janeiro	1000	RJ	49	Ricardo Mendes Pereira	zi3bHyL9wkzXzYXtljwpB+hQTKl4vNHqKP0rIPA8SR8=	uJBrw/csw41uBpwktjcj6A==	teste7890	M	(21) 98765-4321	1.79	Branco	1995-08-25	Pós-graduação	Casado	25.71705	Rio de Janeiro	Brasileiro	SSP-RJ	82.4	90.123.876-5	A	RJ	RJ	0
\.


--
-- Name: paciente_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.paciente_id_seq', 56, true);


--
-- PostgreSQL database dump complete
--

