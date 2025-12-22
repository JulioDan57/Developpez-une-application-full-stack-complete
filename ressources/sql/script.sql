-- Script SQL pour créer les tables du projet MDD

-- Table: User
CREATE TABLE users (
  user_id INT AUTO_INCREMENT PRIMARY KEY,
  email VARCHAR(255) NOT NULL UNIQUE,
  username VARCHAR(100) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL
);

-- Table: Subject
CREATE TABLE subjects (
  subject_id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  description TEXT NOT NULL
);

-- Table: Article
CREATE TABLE articles (
  article_id INT AUTO_INCREMENT PRIMARY KEY,
  subject_id INT NOT NULL,
  user_id INT NOT NULL,
  title VARCHAR(255) NOT NULL,
  content TEXT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (subject_id) REFERENCES subjects(subject_id),
  FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Table: Comment
CREATE TABLE comments (
  comment_id INT AUTO_INCREMENT PRIMARY KEY,
  article_id INT NOT NULL,
  user_id INT NOT NULL,
  content TEXT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (article_id) REFERENCES articles(article_id),
  FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Table: Subscription
CREATE TABLE subscriptions (
  subscription_id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  subject_id INT NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users(user_id),
  FOREIGN KEY (subject_id) REFERENCES subjects(subject_id)
);

ALTER TABLE subscriptions ADD CONSTRAINT unique_user_subject UNIQUE (user_id, subject_id);

-- Insertion de données de test pour la table "subjects"
INSERT INTO subjects (name, description)
VALUES
  ('JavaScript', 'Langage de programmation pour le développement web.'),
  ('Java', 'Langage polyvalent orienté objet utilisé dans divers domaines.'),
  ('Python', 'Langage interprété largement utilisé en développement logiciel.'),
  ('Web3', 'Nouvelle version des technologies web avec un focus sur la décentralisation.'),
  ('C++', 'Langage de programmation pour le développement logiciel et les jeux vidéo.'),
  ('Data Science', 'Domaine utilisant des techniques statistiques pour l analyse de données.'),
  ('Mobile App Development', 'Création d applications pour les appareils mobiles.'),
  ('Artificial Intelligence', 'Simulation de processus intelligents par des machines.'),
  ('Cybersecurity', 'Protection des systèmes informatiques contre les menaces.');
