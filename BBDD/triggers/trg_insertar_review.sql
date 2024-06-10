IF OBJECT_ID('trg_insertar_review', 'TR') IS NOT NULL
    DROP TRIGGER trg_insertar_review;
GO

CREATE TRIGGER trg_insertar_review 
ON reviews
AFTER INSERT
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @questionary_points INT;

    SELECT @questionary_points = points_reward
    FROM inserted AS i
    JOIN questionaries AS q ON i.id_questionary = q.id_questionary;

    UPDATE users 
    SET points = points + @questionary_points
    WHERE email = (SELECT email FROM inserted);
END;
GO