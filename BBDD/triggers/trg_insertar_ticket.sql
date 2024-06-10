IF OBJECT_ID('trg_insertar_ticket', 'TR') IS NOT NULL
    DROP TRIGGER trg_insertar_ticket;
GO

CREATE TRIGGER trg_insertar_ticket 
ON ticket
INSTEAD OF INSERT
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @points_act INT;
    DECLARE @price INT;
    DECLARE @current_stock INT;

    SELECT @points_act = points
    FROM users
    WHERE email = (SELECT email FROM inserted);

    SELECT @price = rewards_price,
           @current_stock = stock
    FROM rewards
    WHERE id_reward = (SELECT id_reward FROM inserted) and company_code=(SELECT company_code FROM inserted);

    IF @points_act >= @price AND @current_stock > 0
    BEGIN
        UPDATE rewards 
        SET stock = stock - 1 
        WHERE id_reward = (SELECT id_reward FROM inserted) and company_code=(SELECT company_code FROM inserted);

        UPDATE users 
        SET points = points - @price 
        WHERE email = (SELECT email FROM inserted);
    END
    ELSE
    BEGIN
        RAISERROR ('No hay suficientes puntos o stock para realizar esta transacción', 16, 1);
        ROLLBACK TRANSACTION;
    END
END;
GO
