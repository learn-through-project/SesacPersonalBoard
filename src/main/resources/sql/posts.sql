use hanpost;

DELIMITER //
CREATE PROCEDURE InsertMultiplePost()
BEGIN
    DECLARE i INT DEFAULT 1;

    WHILE i < 50 DO
        insert into Posts (user_id, title, text_content)
        values(1, CONCAT('post ' , i), CONCAT('this is smaple', i));

        SET i = i + 1;
    END WHILE;

END //

DELIMITER ;

CALL InsertMultiplePost();


DROP PROCEDURE InsertMultiplePost;
