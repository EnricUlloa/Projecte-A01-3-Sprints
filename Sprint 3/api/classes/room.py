from pydantic import BaseModel
from datetime import datetime

class Room (BaseModel):
  code: str