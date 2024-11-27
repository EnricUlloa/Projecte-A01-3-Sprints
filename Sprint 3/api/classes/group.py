from pydantic import BaseModel
from typing import List

class Group (BaseModel):
  name: str
  
class GroupedUsers(BaseModel):
  name: str
  users: List[int]