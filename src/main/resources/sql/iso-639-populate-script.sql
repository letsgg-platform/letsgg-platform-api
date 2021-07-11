-- Table --
CREATE TABLE IF NOT EXISTS locale
(
    id     SMALLINT PRIMARY KEY,
    name   VARCHAR(49) DEFAULT NULL,
    alpha2 CHAR(2)     DEFAULT NULL
);

-- locale --
INSERT INTO locale
VALUES (1, 'English', 'en');
INSERT INTO locale
VALUES (2, 'Afar', 'aa');
INSERT INTO locale
VALUES (3, 'Abkhazian', 'ab');
INSERT INTO locale
VALUES (4, 'Afrikaans', 'af');
INSERT INTO locale
VALUES (5, 'Amharic', 'am');
INSERT INTO locale
VALUES (6, 'Arabic', 'ar');
INSERT INTO locale
VALUES (7, 'Assamese', 'as');
INSERT INTO locale
VALUES (8, 'Aymara', 'ay');
INSERT INTO locale
VALUES (9, 'Azerbaijani', 'az');
INSERT INTO locale
VALUES (10, 'Bashkir', 'ba');
INSERT INTO locale
VALUES (11, 'Belarusian', 'be');
INSERT INTO locale
VALUES (12, 'Bulgarian', 'bg');
INSERT INTO locale
VALUES (13, 'Bihari', 'bh');
INSERT INTO locale
VALUES (14, 'Bislama', 'bi');
INSERT INTO locale
VALUES (15, 'Bengali/Bangla', 'bn');
INSERT INTO locale
VALUES (16, 'Tibetan', 'bo');
INSERT INTO locale
VALUES (17, 'Breton', 'br');
INSERT INTO locale
VALUES (18, 'Catalan', 'ca');
INSERT INTO locale
VALUES (19, 'Corsican', 'co');
INSERT INTO locale
VALUES (20, 'Czech', 'cs');
INSERT INTO locale
VALUES (21, 'Welsh', 'cy');
INSERT INTO locale
VALUES (22, 'Danish', 'da');
INSERT INTO locale
VALUES (23, 'German', 'de');
INSERT INTO locale
VALUES (24, 'Bhutani', 'dz');
INSERT INTO locale
VALUES (25, 'Greek', 'el');
INSERT INTO locale
VALUES (26, 'Esperanto', 'eo');
INSERT INTO locale
VALUES (27, 'Spanish', 'es');
INSERT INTO locale
VALUES (28, 'Estonian', 'et');
INSERT INTO locale
VALUES (29, 'Basque', 'eu');
INSERT INTO locale
VALUES (30, 'Persian', 'fa');
INSERT INTO locale
VALUES (31, 'Finnish', 'fi');
INSERT INTO locale
VALUES (32, 'Fiji', 'fj');
INSERT INTO locale
VALUES (33, 'Faeroese', 'fo');
INSERT INTO locale
VALUES (34, 'French', 'fr');
INSERT INTO locale
VALUES (35, 'Frisian', 'fy');
INSERT INTO locale
VALUES (36, 'Irish', 'ga');
INSERT INTO locale
VALUES (37, 'Scots/Gaelic', 'gd');
INSERT INTO locale
VALUES (38, 'Galician', 'gl');
INSERT INTO locale
VALUES (39, 'Guarani', 'gn');
INSERT INTO locale
VALUES (40, 'Gujarati', 'gu');
INSERT INTO locale
VALUES (41, 'Hausa', 'ha');
INSERT INTO locale
VALUES (42, 'Hindi', 'hi');
INSERT INTO locale
VALUES (43, 'Croatian', 'hr');
INSERT INTO locale
VALUES (44, 'Hungarian', 'hu');
INSERT INTO locale
VALUES (45, 'Armenian', 'hy');
INSERT INTO locale
VALUES (46, 'Interlingua', 'ia');
INSERT INTO locale
VALUES (47, 'Interlingue', 'ie');
INSERT INTO locale
VALUES (48, 'Inupiak', 'ik');
INSERT INTO locale
VALUES (49, 'Indonesian', 'in');
INSERT INTO locale
VALUES (50, 'Icelandic', 'is');
INSERT INTO locale
VALUES (51, 'Italian', 'it');
INSERT INTO locale
VALUES (52, 'Hebrew', 'iw');
INSERT INTO locale
VALUES (53, 'Japanese', 'ja');
INSERT INTO locale
VALUES (54, 'Yiddish', 'ji');
INSERT INTO locale
VALUES (55, 'Javanese', 'jw');
INSERT INTO locale
VALUES (56, 'Georgian', 'ka');
INSERT INTO locale
VALUES (57, 'Kazakh', 'kk');
INSERT INTO locale
VALUES (58, 'Greenlandic', 'kl');
INSERT INTO locale
VALUES (59, 'Cambodian', 'km');
INSERT INTO locale
VALUES (60, 'Kannada', 'kn');
INSERT INTO locale
VALUES (61, 'Korean', 'ko');
INSERT INTO locale
VALUES (62, 'Kashmiri', 'ks');
INSERT INTO locale
VALUES (63, 'Kurdish', 'ku');
INSERT INTO locale
VALUES (64, 'Kirghiz', 'ky');
INSERT INTO locale
VALUES (65, 'Latin', 'la');
INSERT INTO locale
VALUES (66, 'Lingala', 'ln');
INSERT INTO locale
VALUES (67, 'Laothian', 'lo');
INSERT INTO locale
VALUES (68, 'Lithuanian', 'lt');
INSERT INTO locale
VALUES (69, 'Latvian/Lettish', 'lv');
INSERT INTO locale
VALUES (70, 'Malagasy', 'mg');
INSERT INTO locale
VALUES (71, 'Maori', 'mi');
INSERT INTO locale
VALUES (72, 'Macedonian', 'mk');
INSERT INTO locale
VALUES (73, 'Malayalam', 'ml');
INSERT INTO locale
VALUES (74, 'Mongolian', 'mn');
INSERT INTO locale
VALUES (75, 'Moldavian', 'mo');
INSERT INTO locale
VALUES (76, 'Marathi', 'mr');
INSERT INTO locale
VALUES (77, 'Malay', 'ms');
INSERT INTO locale
VALUES (78, 'Maltese', 'mt');
INSERT INTO locale
VALUES (79, 'Burmese', 'my');
INSERT INTO locale
VALUES (80, 'Nauru', 'na');
INSERT INTO locale
VALUES (81, 'Nepali', 'ne');
INSERT INTO locale
VALUES (82, 'Dutch', 'nl');
INSERT INTO locale
VALUES (83, 'Norwegian', 'no');
INSERT INTO locale
VALUES (84, 'Occitan', 'oc');
INSERT INTO locale
VALUES (85, '(Afan)/Oromoor/Oriya', 'om');
INSERT INTO locale
VALUES (86, 'Punjabi', 'pa');
INSERT INTO locale
VALUES (87, 'Polish', 'pl');
INSERT INTO locale
VALUES (88, 'Pashto/Pushto', 'ps');
INSERT INTO locale
VALUES (89, 'Portuguese', 'pt');
INSERT INTO locale
VALUES (90, 'Quechua', 'qu');
INSERT INTO locale
VALUES (91, 'Rhaeto-Romance', 'rm');
INSERT INTO locale
VALUES (92, 'Kirundi', 'rn');
INSERT INTO locale
VALUES (93, 'Romanian', 'ro');
INSERT INTO locale
VALUES (94, 'Russian', 'ru');
INSERT INTO locale
VALUES (95, 'Kinyarwanda', 'rw');
INSERT INTO locale
VALUES (96, 'Sanskrit', 'sa');
INSERT INTO locale
VALUES (97, 'Sindhi', 'sd');
INSERT INTO locale
VALUES (98, 'Sangro', 'sg');
INSERT INTO locale
VALUES (99, 'Serbo-Croatian', 'sh');
INSERT INTO locale
VALUES (100, 'Singhalese', 'si');
INSERT INTO locale
VALUES (101, 'Slovak', 'sk');
INSERT INTO locale
VALUES (102, 'Slovenian', 'sl');
INSERT INTO locale
VALUES (103, 'Samoan', 'sm');
INSERT INTO locale
VALUES (104, 'Shona', 'sn');
INSERT INTO locale
VALUES (105, 'Somali', 'so');
INSERT INTO locale
VALUES (106, 'Albanian', 'sq');
INSERT INTO locale
VALUES (107, 'Serbian', 'sr');
INSERT INTO locale
VALUES (108, 'Siswati', 'ss');
INSERT INTO locale
VALUES (109, 'Sesotho', 'st');
INSERT INTO locale
VALUES (110, 'Sundanese', 'su');
INSERT INTO locale
VALUES (111, 'Swedish', 'sv');
INSERT INTO locale
VALUES (112, 'Swahili', 'sw');
INSERT INTO locale
VALUES (113, 'Tamil', 'ta');
INSERT INTO locale
VALUES (114, 'Telugu', 'te');
INSERT INTO locale
VALUES (115, 'Tajik', 'tg');
INSERT INTO locale
VALUES (116, 'Thai', 'th');
INSERT INTO locale
VALUES (117, 'Tigrinya', 'ti');
INSERT INTO locale
VALUES (118, 'Turkmen', 'tk');
INSERT INTO locale
VALUES (119, 'Tagalog', 'tl');
INSERT INTO locale
VALUES (120, 'Setswana', 'tn');
INSERT INTO locale
VALUES (121, 'Tonga', 'to');
INSERT INTO locale
VALUES (122, 'Turkish', 'tr');
INSERT INTO locale
VALUES (123, 'Tsonga', 'ts');
INSERT INTO locale
VALUES (124, 'Tatar', 'tt');
INSERT INTO locale
VALUES (125, 'Twi', 'tw');
INSERT INTO locale
VALUES (126, 'Ukrainian', 'uk');
INSERT INTO locale
VALUES (127, 'Urdu', 'ur');
INSERT INTO locale
VALUES (128, 'Uzbek', 'uz');
INSERT INTO locale
VALUES (129, 'Vietnamese', 'vi');
INSERT INTO locale
VALUES (130, 'Volapuk', 'vo');
INSERT INTO locale
VALUES (131, 'Wolof', 'wo');
INSERT INTO locale
VALUES (132, 'Xhosa', 'xh');
INSERT INTO locale
VALUES (133, 'Yoruba', 'yo');
INSERT INTO locale
VALUES (134, 'Chinese', 'zh');
INSERT INTO locale
VALUES (135, 'Zulu', 'zu');