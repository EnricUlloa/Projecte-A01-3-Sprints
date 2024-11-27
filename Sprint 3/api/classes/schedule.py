from pydantic import BaseModel
from datetime import datetime

class Schedule(BaseModel):
  id: int
  manager_id: int
  group_name: str
  room_code: str
  week_day: str
  start_time: datetime
  end_time: datetime
  
