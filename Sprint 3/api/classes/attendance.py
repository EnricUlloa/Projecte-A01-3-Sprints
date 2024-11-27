from pydantic import BaseModel
from datetime import datetime

class Attendance (BaseModel):
  id: int
  user_id: int
  check_time: datetime
  schedule_id: int
  type: str