use hanpost;

insert into Posts (user_id, text_content) values(1, 'this is smaple');

DELIMITER //
CREATE PROCEDURE InsertMultipleComments()
BEGIN
    DECLARE i INT DEFAULT 0;

    WHILE i < 100 DO
        insert into Posts (user_id, text_content)
        values(1, CONCAT('this is smaple', i));

        SET i = i + 1;
    END WHILE;

END //

DELIMITER ;

CALL InsertMultipleComments();


DROP PROCEDURE InsertMultipleComments;
