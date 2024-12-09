from typing import Optional
from api.internal import db_client, db_exception_handler


@db_exception_handler
def read(
  name: Optional[str],  orderby: Optional[str], 
  skip: int = 0, limit: int = 0):
  
  with db_client() as conn: # cliente de conexion a la bd
    cur = conn.cursor() # ejecutor / cursor
    query = """
      SELECT 
        u.id,
        u.email,
        u.full_name,
        u.rol,
        GROUP_CONCAT(gu.group_name ORDER BY gu.group_name SEPARATOR ',') AS groups
      FROM 
        user u
      LEFT JOIN 
        grouped_users gu ON u.id = gu.user_id
    """
    
    params = []
    
    # Añadir filtro por "name"
    if name is not None:
      query += " WHERE u.full_name LIKE %s"
      params.append(f"%{name}%")
    
    # Agrega la agrupación
    query += """
      GROUP BY 
        u.id, u.email, u.full_name, u.rol;
    """
    
    # Añadir orden
    if orderby is not None:
      query += " ORDER BY u.nom " + orderby
    
    # Añadir limit y offset
    if limit > 0:
      query += " LIMIT %s"
      params.append(limit)
      if skip > 0: # skip only if has limit
        query += " OFFSET %s"
        params.append(skip)
    

    cur.execute(query, tuple(params)) # ejecuta y devuelve un generador de datos para python | interfaz
    users = cur.fetchall() # Lista de valores valor[][]
    
    return users


@db_exception_handler
def read_one(id: int):
  
  with db_client() as conn:
    cur = conn.cursor()
    query = """
      SELECT 
        u.id,
        u.email,
        u.full_name,
        u.rol,
        GROUP_CONCAT(gu.group_name ORDER BY gu.group_name SEPARATOR ',') AS groups
      FROM 
        user u
      LEFT JOIN 
        grouped_users gu ON u.id = gu.user_id
      WHERE
        u.id = %s
      GROUP BY 
        u.id, u.email, u.full_name, u.rol;
    """
    
    params = (id,)
    cur.execute(query, params)
    
    return cur.fetchone()
  