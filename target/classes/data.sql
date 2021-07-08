INSERT INTO company (name) VALUES ('APP_ADMIN');
INSERT INTO company (name) VALUES ('Company1');
INSERT INTO company (name) VALUES ('Company2');

INSERT INTO user (email,fullname, password_hash, role,username,companyid) VALUES ('aly.hhp.6533@gmail.com', 	'Admin','$2a$10$6QtV6STBztNcRkqGapgfxuBS96QICKNHy/VmGx0wu/N1l3lG8wb/.', 'Administrator','admin'	,1);
INSERT INTO user (email,fullname, password_hash, role,username,companyid) VALUES ('a@a.a', 			'a',	'$2a$10$6QtV6STBztNcRkqGapgfxuBS96QICKNHy/VmGx0wu/N1l3lG8wb/.', 'User',			'a'	,2);
INSERT INTO user (email,fullname, password_hash, role,username,companyid) VALUES ('b@b.b', 			'a',	'$2a$10$6QtV6STBztNcRkqGapgfxuBS96QICKNHy/VmGx0wu/N1l3lG8wb/.', 'User',			'b'	,2);
INSERT INTO user (email,fullname, password_hash, role,username,companyid) VALUES ('c@c.c', 			'a',	'$2a$10$6QtV6STBztNcRkqGapgfxuBS96QICKNHy/VmGx0wu/N1l3lG8wb/.', 'User',			'c'	,3);
INSERT INTO user (email,fullname, password_hash, role,username,companyid) VALUES ('d@d.d', 			'a',	'$2a$10$6QtV6STBztNcRkqGapgfxuBS96QICKNHy/VmGx0wu/N1l3lG8wb/.', 'User',			'd'	,3);