from pydantic import BaseModel
from datetime import datetime

class Attendance (BaseModel):
  id: int
  user_id: int
  check_time: datetime
  schedule_id: int
  type: str
  
class AttendanceInfo (BaseModel):
  user_name: str
  group_name: str
  room_code: str
  check_time: datetime
  type: str
  registrer_name: str
  
VALID_TYPES = ('attended','late','missed','justified')