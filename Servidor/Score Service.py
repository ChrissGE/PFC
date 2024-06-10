import pyodbc as odbc

server = '(localdb)\localhostAdrian'
server = '(localdb)\opinapp'
database = 'OpinAppDB'

def create_connection():
    try:
        conn = odbc.connect(Trusted_Connection='Yes',Driver='{ODBC Driver 17 for SQL Server}',Server=server,Database=database)
        print("Conexión exitosa")
        return conn
    except Exception as e:
        print("Error al conectar:", e)
        return None

conn = create_connection()

def calculate_global_score(company_code):
    cursor2 = conn.cursor()
    try:
        sql_query = """
            SELECT c.company_code, mgs.company_code, mgs.id_scoring, mgs.percentage, 
            sv.id_scoring_value, sv.id_scoring, sv.mark
            FROM company c
            INNER JOIN map_global_scorings mgs ON mgs.company_code = c.company_code
            INNER JOIN scorings_value sv ON sv.id_scoring = mgs.id_scoring
            WHERE c.company_code = ?
            AND sv.id_scoring_value = (
                SELECT MAX(id_scoring_value)
                FROM scorings_value
                WHERE id_scoring = mgs.id_scoring)
        """     
        cursor2.execute(sql_query, company_code)
        rows = cursor2.fetchall()
        score = 0
        for row in rows:
            if row.mark is not None:
                score += (row.mark*(row.percentage/100))
        sql_query="""insert into global_scorings_value(company_code,mark) values (?,?)"""
        cursor2.execute(sql_query,(company_code,round(score, 2)))
    finally:
        cursor2.close()



def calculate_scoreMenu(id_mapQuestionary):
    cursor2 = conn.cursor()
    try:
        sql_query = """
            select * from scoring_per_map_review spmr
            inner join questionaryMenu mq on mq.id_questionaryMenu=spmr.id_questionaryMenu
            where spmr.id_questionaryMenu=?
        """     
        cursor2.execute(sql_query, id_mapQuestionary)
        rows = cursor2.fetchall()
        score = 0
        rows_count = 0
        for row in rows:
            if row.mark is not None:
                score += row.mark
                rows_count += 1

        if rows_count > 0:
            score /= rows_count
        return score
    finally:
        cursor2.close()

#TODO: esto hay que mirarlo poprque el puto adrian ha metido scoring_value
def calculate_score(company_code):
    cursor = conn.cursor()
    try:
        sql_query = """
            select s.id_questionaryMenu, s.id_scoring from questionaries q
            inner join questionaryMenu mq on mq.id_questionary=q.id_questionary
            inner join scorings s on s.id_questionaryMenu=mq.id_questionaryMenu
            where q.company_code = ?
        """     
        cursor.execute(sql_query,company_code)
        rows = cursor.fetchall()
        for row in rows:
            score = calculate_scoreMenu(row.id_questionaryMenu)
            sql_query="""insert into scorings_value(id_scoring,mark) values(?,?)"""
            cursor.execute(sql_query,(row.id_scoring,round(score, 2)))
        conn.commit()
        calculate_global_score(company_code)
    finally:
        cursor.close()

def check_changes():
    cursor = conn.cursor()
    try:
        sql_query = """
            select * from traces
        """     
        cursor.execute(sql_query)
        rows = cursor.fetchall()
        if rows is not None:
            for row in rows:
                calculate_score(row.company_code)
            cursor.execute('delete traces')
            conn.commit()
    finally:
        print('No traces')
        cursor.close()

check_changes()
#pruebas
