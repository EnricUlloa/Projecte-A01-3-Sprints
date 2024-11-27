from pydantic import BaseModel
from typing import List

class User(BaseModel):
  id: int
  email: str
  full_name: str
  password: str
  rfid_uid: str
  rol: str
  
class UserInfo(BaseModel):
  id: int
  email: str
  full_name: str
  rol: str
  groups: List[str]