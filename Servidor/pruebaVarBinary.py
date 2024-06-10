from PIL import Image
import io
import pyodbc as odbc

#erver = '(localdb)\localhostAdrian'
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

def toVarBinary(image_path,id,image_type,query):
    conn = create_connection()
    # Carga la imagen
    image = Image.open(image_path)

    if image.mode != 'RGB':
        image = image.convert('RGB')

    # Convierte la imagen a bytes
    image_bytes = io.BytesIO()
    image.save(image_bytes, format=image_type)  # Cambia 'JPEG' al formato de tu imagen si es diferente
    image_bytes = image_bytes.getvalue()
    cursor = conn.cursor()
    cursor.execute(query, (image_bytes,id))
    conn.commit()

    # Cierra la conexión
    cursor.close()
    conn.close()

toVarBinary(r'C:\Users\chris\Desktop\Server\OpinApp Server\imagenPrueba\star.png','1','png',"update rewards set image_reward=? where id_reward=?")
toVarBinary(r'C:\Users\chris\Desktop\Server\OpinApp Server\imagenPrueba\star.png','2','png',"update rewards set image_reward=? where id_reward=?")
toVarBinary(r'C:\Users\chris\Desktop\Server\OpinApp Server\imagenPrueba\star.png','3','png',"update rewards set image_reward=? where id_reward=?")

toVarBinary(r'C:\Users\chris\Desktop\Server\OpinApp Server\imagenPrueba\star.png','E73463259','png',"update company set image_company=? where company_code=?")
toVarBinary(r'C:\Users\chris\Desktop\Server\OpinApp Server\imagenPrueba\star.png','U31477490','png',"update company set image_company=? where company_code=?")