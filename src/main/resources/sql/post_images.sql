
DELIMITER //
CREATE PROCEDURE InsertMultiplePostImages()
BEGIN
    DECLARE i INT DEFAULT 1;

    WHILE i < 5 DO
        insert into post_images (post_id, url)
        values(i, CONCAT('https://picsum.photos/id/', i, '/200/300'));

        SET i = i + 1;
    END WHILE;

END //

DELIMITER ;

CALL InsertMultiplePostImages();


DROP PROCEDURE InsertMultiplePostImages;
