from typing import List, Optional
from fastapi import APIRouter, HTTPException
from api.classes import AttendanceInfo
from api.internal import db_attendance, attendance_schema as AttendanceSchema

router = APIRouter()

@router.get("/attendances", response_model=List[AttendanceInfo])
def attendance_info(
  type: str = "",
  room: Optional[str] = None,
  group: Optional[str] = None,
  user_id: int = 0,
):
  type_list = type.split(",") if type else []
  results = db_attendance.read_attendances_info(
    type_list, room, group, user_id
  )
  
  if results is None:
    return []
  
  return AttendanceSchema.attendances_info_schema(results)
