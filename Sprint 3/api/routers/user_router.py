from typing import List, Optional
from fastapi import APIRouter, HTTPException
from api.classes import UserInfo
from api.internal import db_user, user_schema as UserSchema

router = APIRouter()


@router.get("/users", response_model=List[UserInfo])
def list_users(
  name: Optional[str] = None, 
  orderby: Optional[str] = None,  
  skip: int = 0, 
  limit: int = 0
  ):
  
  result = db_user.read(name, orderby, skip, limit)
  
  users = UserSchema.users_info_schema(result)
  return users

@router.get("/user/{id}", response_model=UserInfo)
def list_user(
  id: int
  ):
  
  result = db_user.read_one(id)
  
  user = UserSchema.user_info_schema(result)
  return user

