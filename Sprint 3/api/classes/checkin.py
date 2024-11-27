from pydantic import BaseModel
from datetime import datetime

class Checkin (BaseModel):
  id: int
  user_id: int
  room_code: str
  checkin_time: datetime
  
