CREATE TABLE document_label (post_id varchar(25) NOT NULL, topic_id int(10) NOT NULL, weight double NOT NULL, PRIMARY KEY (post_id, topic_id));
CREATE TABLE topic (id int(10) NOT NULL AUTO_INCREMENT, PRIMARY KEY (id));
CREATE TABLE term (id int(10) NOT NULL AUTO_INCREMENT, term varchar(50) NOT NULL, PRIMARY KEY (id));
CREATE TABLE topic_relationship (topicid_A int(10) NOT NULL, topicid_B int(10) NOT NULL, weight double NOT NULL, PRIMARY KEY (topicid_A, topicid_B));
CREATE TABLE topic_term (topicid int(10) NOT NULL, termid int(10) NOT NULL, weight double NOT NULL, PRIMARY KEY (topicid, termid));

