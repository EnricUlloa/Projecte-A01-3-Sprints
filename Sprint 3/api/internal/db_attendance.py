from typing import Optional, List
from api.internal import db_client, db_exception_handler
from api.classes.attendance import VALID_TYPES

@db_exception_handler
def read_attendances_info(
  type: Optional[List[str]],
  room: Optional[str],
  group: Optional[str],
  user_id: int = 0,
):
  
  with db_client() as conn:
    cur = conn.cursor()
    query = """
      SELECT 
        u.full_name as User,
        sch.group_name,
        sch.room_code,
        at.check_time,
        at.type,
        reg.full_name as Register
      FROM 
        attendance at 
        LEFT JOIN user u
          ON (at.user_id = u.id)
        LEFT JOIN user reg
          ON (at.registrer_id = reg.id)
        LEFT JOIN schedule sch
          ON (at.schedule_id = sch.id)"""
    
    params = []
    
    if (
      type is not None or 
      room is not None or 
      group is not None or
      user_id > 0
      ):
      query += "\nWHERE "
    
      # Busca que sea de cualquiera de los tipos
      if type is not None and len(type) > 0:
        type_conditions = []
        for t in type:
          if t in VALID_TYPES:
            type_conditions.append("at.type = %s")
            params.append(t)
        if type_conditions:
          query += "(" + " OR ".join(type_conditions) + ") and "
        
      # Que sea de la room X
      if room is not None:
        query += "\n(at.room_code = %s) and "
        params.append(room)
      
      # Del grupo X
      if group is not None:
        query += "\n(sch.group_name = %s) and "
        params.append(group)
        
      # Solo del usuario X
      if user_id > 0:
        query += "\n(at.user_id = %s) and "
        params.append(user_id)
        
      query += "true"
    
      
    print(query)
    cur.execute(query, tuple(params))
    
    info = cur.fetchall()
    return info


