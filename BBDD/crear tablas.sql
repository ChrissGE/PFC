IF OBJECT_ID('map_global_scorings', 'U') IS NOT NULL
    DROP TABLE map_global_scorings;
IF OBJECT_ID('global_scorings_value', 'U') IS NOT NULL
    DROP TABLE global_scorings_value;
IF OBJECT_ID('traces', 'U') IS NOT NULL
    DROP TABLE traces;
IF OBJECT_ID('answer', 'U') IS NOT NULL
    DROP TABLE answer;
IF OBJECT_ID('scoring_per_map_review', 'U') IS NOT NULL
	DROP TABLE scoring_per_map_review;
IF OBJECT_ID('scorings_value', 'U') IS NOT NULL
    DROP TABLE scorings_value;
IF OBJECT_ID('scorings', 'U') IS NOT NULL
    DROP TABLE scorings;
IF OBJECT_ID('mapTextReward', 'U') IS NOT NULL
    DROP TABLE mapTextReward;
IF OBJECT_ID('mapTextQuestions', 'U') IS NOT NULL
    DROP TABLE mapTextQuestions;
IF OBJECT_ID('mapTextMenu', 'U') IS NOT NULL
    DROP TABLE mapTextMenu;
IF OBJECT_ID('texts', 'U') IS NOT NULL
    DROP TABLE texts;
IF OBJECT_ID('languages', 'U') IS NOT NULL
    DROP TABLE languages;
IF OBJECT_ID('reviews', 'U') IS NOT NULL
    DROP TABLE reviews;
IF OBJECT_ID('question_condition', 'U') IS NOT NULL
    DROP TABLE question_condition;
IF OBJECT_ID('mapQuestions', 'U') IS NOT NULL
    DROP TABLE mapQuestions;
IF OBJECT_ID('questions', 'U') IS NOT NULL
	DROP TABLE questions;
IF OBJECT_ID('questionaryMenu', 'U') IS NOT NULL
    DROP TABLE questionaryMenu;
IF OBJECT_ID('questionaries', 'U') IS NOT NULL
    DROP TABLE questionaries;
IF OBJECT_ID('ticket', 'U') IS NOT NULL
    DROP TABLE ticket;
IF OBJECT_ID('rewards', 'U') IS NOT NULL
    DROP TABLE rewards;
IF OBJECT_ID('company', 'U') IS NOT NULL
    DROP TABLE company;
IF OBJECT_ID('users', 'U') IS NOT NULL
    DROP TABLE users;
IF OBJECT_ID('languages', 'U') IS NOT NULL
    DROP TABLE languages;
IF OBJECT_ID('texts', 'U') IS NOT NULL
    DROP TABLE texts;
IF OBJECT_ID('mapTextQuestions', 'U') IS NOT NULL
    DROP TABLE mapTextQuestions;
IF OBJECT_ID('mapTextReward', 'U') IS NOT NULL
    DROP TABLE mapTextReward;

CREATE TABLE users (
    email VARCHAR(50) PRIMARY KEY,
	username varchar(25),
    gender VARCHAR(10) CHECK (gender IN ('Male', 'Female', 'Other')),
    birth_date DATE,
    points INT
);

CREATE TABLE company (
    company_code VARCHAR(20) PRIMARY KEY,
    company_name VARCHAR(50),
    address VARCHAR(max),
    coords VARCHAR(50),
    image_company varBinary(MAX),
    email VARCHAR(50)
);

CREATE TABLE rewards (
    id_reward INT IDENTITY(1,1) PRIMARY KEY,
    rewards_price INT,
    company_code VARCHAR(20),
    stock INT,
    image_reward varBinary(MAX),
    FOREIGN KEY(company_code) REFERENCES company(company_code)
);

CREATE TABLE ticket (
    id_ticket INT IDENTITY(1,1) PRIMARY KEY,
    id_reward INT,
    email VARCHAR(50),
    FOREIGN KEY(id_reward) REFERENCES rewards(id_reward),
    FOREIGN KEY(email) REFERENCES users(email)
);

CREATE TABLE questionaries (
    id_questionary INT IDENTITY(1,1) PRIMARY KEY,
    questionary_name VARCHAR(100),
    points_reward INT,
    company_code VARCHAR(20),
    FOREIGN KEY(company_code) REFERENCES company(company_code)
);

CREATE TABLE questionaryMenu (
    id_questionaryMenu INT IDENTITY(1,1) PRIMARY KEY,
    id_questionary INT,
    FOREIGN KEY(id_questionary) REFERENCES questionaries(id_questionary)
);

CREATE TABLE questions (
    id_questions INT IDENTITY(1,1) PRIMARY KEY,
    question_type varchar(10) CHECK (question_type IN ('Text', 'Yes and no')),
    text VARCHAR(255)
);

CREATE TABLE mapQuestions(
    id_questions INT,
    id_questionaryMenu INT,
    PRIMARY key(id_questions,id_questionaryMenu),
    FOREIGN KEY(id_questions) REFERENCES questions(id_questions),
    FOREIGN KEY(id_questionaryMenu) REFERENCES questionaryMenu(id_questionaryMenu)
);

CREATE TABLE question_condition (
    id_questions INT,
    id_questionaryMenu INT,
    answer_value INT,
    score_value DECIMAL(10,2),
    PRIMARY KEY(id_questions, id_questionaryMenu),
    FOREIGN KEY(id_questions,id_questionaryMenu) REFERENCES mapQuestions(id_questions,id_questionaryMenu)
);

CREATE TABLE reviews (
    id_review INT IDENTITY(1,1) PRIMARY KEY,
    id_questionary INT,
    email VARCHAR(50),
    insert_date DATETIME DEFAULT GETDATE(),
    FOREIGN KEY(id_questionary) REFERENCES questionaries(id_questionary),
    FOREIGN KEY(email) REFERENCES users(email)
);

create table languages(
id_language INT IDENTITY(1,1) PRIMARY KEY,
name_language VARCHAR(50)
);

create table texts(
    id_text INT IDENTITY(1,1) PRIMARY KEY,
    text VARCHAR(200),
    id_language INT,
    FOREIGN KEY(id_language) REFERENCES languages(id_language)
);
create table mapTextQuestions(
    id_text INT,
    id_questions INT,
    PRIMARY key(id_text,id_questions),
    FOREIGN KEY(id_text) REFERENCES texts(id_text),
    FOREIGN KEY(id_questions) REFERENCES questions(id_questions)
);

create table mapTextReward(
    id_text INT,
    id_reward INT,
    PRIMARY key(id_text,id_reward),
    FOREIGN KEY(id_text) REFERENCES texts(id_text),
    FOREIGN KEY(id_reward) REFERENCES rewards(id_reward)
);
create table mapTextMenu(
    id_text INT,
    id_questionaryMenu INT,
    PRIMARY key(id_text,id_questionaryMenu),
    FOREIGN KEY(id_text) REFERENCES texts(id_text),
    FOREIGN KEY(id_questionaryMenu) REFERENCES questionaryMenu(id_questionaryMenu)
);
CREATE TABLE answer (
    id_answer INT IDENTITY(1,1) PRIMARY KEY,
    id_questions INT,
    id_questionaryMenu INT,
    id_review INT,
    text VARCHAR(255),
    binary_answer INT,
    FOREIGN KEY(id_questions,id_questionaryMenu) REFERENCES mapQuestions(id_questions,id_questionaryMenu),
    FOREIGN KEY(id_review) REFERENCES reviews(id_review)
);

CREATE TABLE scorings (
    id_scoring INT IDENTITY(1,1) PRIMARY KEY,
    scoring_name VARCHAR(160),
    id_questionaryMenu INT,
    FOREIGN KEY(id_questionaryMenu) REFERENCES questionaryMenu(id_questionaryMenu)
);

CREATE TABLE scorings_value(
    id_scoring_value INT IDENTITY(1,1) PRIMARY KEY,
    id_scoring INT,
    mark DECIMAL(10,2),
    FOREIGN KEY(id_scoring) REFERENCES scorings(id_scoring)
);

CREATE TABLE scoring_per_map_review (
    id_scoring_per_review INT IDENTITY(1,1) PRIMARY KEY,
    id_review INT,
    id_questionaryMenu INT,
    mark DECIMAL(10,2),
    FOREIGN KEY(id_review) REFERENCES reviews(id_review),
    FOREIGN KEY(id_questionaryMenu) REFERENCES questionaryMenu(id_questionaryMenu)
);

CREATE TABLE traces(
    company_code VARCHAR(20) PRIMARY KEY,
    FOREIGN KEY(company_code) REFERENCES company(company_code)
);

CREATE TABLE global_scorings_value(
    id_global_scoring_value INT IDENTITY(1,1) PRIMARY KEY,
    company_code VARCHAR(20),
    mark DECIMAL(10,2),
    FOREIGN KEY(company_code) REFERENCES company(company_code)
);

CREATE TABLE map_global_scorings(
    company_code VARCHAR(20),
    id_scoring INT,
    percentage DECIMAL(10,2),
    PRIMARY KEY(company_code,id_scoring),
    FOREIGN KEY(company_code) REFERENCES company(company_code),
    FOREIGN KEY(id_scoring) REFERENCES scorings(id_scoring)
);