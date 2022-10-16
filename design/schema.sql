--
-- PostgreSQL database dump
--

-- Dumped from database version 10.12 (Ubuntu 10.12-0ubuntu0.18.04.1)
-- Dumped by pg_dump version 10.12 (Ubuntu 10.12-0ubuntu0.18.04.1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'LATIN1';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: studyplan; Type: SCHEMA; Schema: -; Owner: dab_di19202b_115
--

CREATE SCHEMA studyplan;


ALTER SCHEMA studyplan OWNER TO dab_di19202b_115;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: courseexecution; Type: TABLE; Schema: studyplan; Owner: dab_di19202b_115
--

CREATE TABLE studyplan.courseexecution (
    year integer NOT NULL,
    block integer NOT NULL,
    sp_id character varying(255),
    course_code integer
);


ALTER TABLE studyplan.courseexecution OWNER TO dab_di19202b_115;

--
-- Name: courses; Type: TABLE; Schema: studyplan; Owner: dab_di19202b_115
--

CREATE TABLE studyplan.courses (
    course_code integer NOT NULL,
    course_name text,
    ec integer
);


ALTER TABLE studyplan.courses OWNER TO dab_di19202b_115;

--
-- Name: form; Type: TABLE; Schema: studyplan; Owner: dab_di19202b_115
--

CREATE TABLE studyplan.form (
    form_id character varying(255) NOT NULL,
    submitiondate date,
    credits integer,
    firsttime boolean,
    intership boolean,
    s_number character varying(255)
);


ALTER TABLE studyplan.form OWNER TO dab_di19202b_115;

--
-- Name: mentor; Type: TABLE; Schema: studyplan; Owner: dab_di19202b_115
--

CREATE TABLE studyplan.mentor (
    employee_no character varying(255) NOT NULL,
    tel_no integer
);


ALTER TABLE studyplan.mentor OWNER TO dab_di19202b_115;

--
-- Name: student; Type: TABLE; Schema: studyplan; Owner: dab_di19202b_115
--

CREATE TABLE studyplan.student (
    s_number character varying(255) NOT NULL,
    program character varying(255)
);


ALTER TABLE studyplan.student OWNER TO dab_di19202b_115;

--
-- Name: studyplan; Type: TABLE; Schema: studyplan; Owner: dab_di19202b_115
--

CREATE TABLE studyplan.studyplan (
    sp_id character varying(255) NOT NULL,
    s_number character varying(255) NOT NULL,
    credits integer,
    intership boolean,
    approved boolean
);


ALTER TABLE studyplan.studyplan OWNER TO dab_di19202b_115;

--
-- Name: user; Type: TABLE; Schema: studyplan; Owner: dab_di19202b_115
--

CREATE TABLE studyplan."user" (
    user_id character varying(255) NOT NULL,
    first_name character varying(255),
    surname character varying(255),
    email character varying(255),
    password character varying(255)
);


ALTER TABLE studyplan."user" OWNER TO dab_di19202b_115;

--
-- Data for Name: courseexecution; Type: TABLE DATA; Schema: studyplan; Owner: dab_di19202b_115
--



--
-- Data for Name: courses; Type: TABLE DATA; Schema: studyplan; Owner: dab_di19202b_115
--



--
-- Data for Name: form; Type: TABLE DATA; Schema: studyplan; Owner: dab_di19202b_115
--



--
-- Data for Name: mentor; Type: TABLE DATA; Schema: studyplan; Owner: dab_di19202b_115
--



--
-- Data for Name: student; Type: TABLE DATA; Schema: studyplan; Owner: dab_di19202b_115
--



--
-- Data for Name: studyplan; Type: TABLE DATA; Schema: studyplan; Owner: dab_di19202b_115
--



--
-- Data for Name: user; Type: TABLE DATA; Schema: studyplan; Owner: dab_di19202b_115
--



--
-- Name: courseexecution courseexecution_pkey; Type: CONSTRAINT; Schema: studyplan; Owner: dab_di19202b_115
--

ALTER TABLE ONLY studyplan.courseexecution
    ADD CONSTRAINT courseexecution_pkey PRIMARY KEY (year, block);


--
-- Name: courses courses_pkey; Type: CONSTRAINT; Schema: studyplan; Owner: dab_di19202b_115
--

ALTER TABLE ONLY studyplan.courses
    ADD CONSTRAINT courses_pkey PRIMARY KEY (course_code);


--
-- Name: form form_pkey; Type: CONSTRAINT; Schema: studyplan; Owner: dab_di19202b_115
--

ALTER TABLE ONLY studyplan.form
    ADD CONSTRAINT form_pkey PRIMARY KEY (form_id);


--
-- Name: mentor mentor_pkey; Type: CONSTRAINT; Schema: studyplan; Owner: dab_di19202b_115
--

ALTER TABLE ONLY studyplan.mentor
    ADD CONSTRAINT mentor_pkey PRIMARY KEY (employee_no);


--
-- Name: student student_pkey; Type: CONSTRAINT; Schema: studyplan; Owner: dab_di19202b_115
--

ALTER TABLE ONLY studyplan.student
    ADD CONSTRAINT student_pkey PRIMARY KEY (s_number);


--
-- Name: studyplan studyplan_pkey; Type: CONSTRAINT; Schema: studyplan; Owner: dab_di19202b_115
--

ALTER TABLE ONLY studyplan.studyplan
    ADD CONSTRAINT studyplan_pkey PRIMARY KEY (sp_id);


--
-- Name: studyplan studyplan_s_number_key; Type: CONSTRAINT; Schema: studyplan; Owner: dab_di19202b_115
--

ALTER TABLE ONLY studyplan.studyplan
    ADD CONSTRAINT studyplan_s_number_key UNIQUE (s_number);


--
-- Name: user user_pkey; Type: CONSTRAINT; Schema: studyplan; Owner: dab_di19202b_115
--

ALTER TABLE ONLY studyplan."user"
    ADD CONSTRAINT user_pkey PRIMARY KEY (user_id);


--
-- Name: courseexecution courseexecution_course_code_fkey; Type: FK CONSTRAINT; Schema: studyplan; Owner: dab_di19202b_115
--

ALTER TABLE ONLY studyplan.courseexecution
    ADD CONSTRAINT courseexecution_course_code_fkey FOREIGN KEY (course_code) REFERENCES studyplan.courses(course_code);


--
-- Name: courseexecution courseexecution_sp_id_fkey; Type: FK CONSTRAINT; Schema: studyplan; Owner: dab_di19202b_115
--

ALTER TABLE ONLY studyplan.courseexecution
    ADD CONSTRAINT courseexecution_sp_id_fkey FOREIGN KEY (sp_id) REFERENCES studyplan.studyplan(sp_id);


--
-- Name: form form_s_number_fkey; Type: FK CONSTRAINT; Schema: studyplan; Owner: dab_di19202b_115
--

ALTER TABLE ONLY studyplan.form
    ADD CONSTRAINT form_s_number_fkey FOREIGN KEY (s_number) REFERENCES studyplan.student(s_number);


--
-- Name: mentor mentor_employee_no_fkey; Type: FK CONSTRAINT; Schema: studyplan; Owner: dab_di19202b_115
--

ALTER TABLE ONLY studyplan.mentor
    ADD CONSTRAINT mentor_employee_no_fkey FOREIGN KEY (employee_no) REFERENCES studyplan."user"(user_id);


--
-- Name: student student_s_number_fkey; Type: FK CONSTRAINT; Schema: studyplan; Owner: dab_di19202b_115
--

ALTER TABLE ONLY studyplan.student
    ADD CONSTRAINT student_s_number_fkey FOREIGN KEY (s_number) REFERENCES studyplan."user"(user_id);


--
-- Name: studyplan studyplan_s_number_fkey; Type: FK CONSTRAINT; Schema: studyplan; Owner: dab_di19202b_115
--

ALTER TABLE ONLY studyplan.studyplan
    ADD CONSTRAINT studyplan_s_number_fkey FOREIGN KEY (s_number) REFERENCES studyplan.student(s_number);


--
-- PostgreSQL database dump complete
--

