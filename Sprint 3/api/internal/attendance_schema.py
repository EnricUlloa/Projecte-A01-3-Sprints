from typing import List
from api.classes import AttendanceInfo

def attendance_info_schema(data) -> AttendanceInfo:
    return {
        "user_name": data[0],
        "group_name": data[1],
        "room_code": data[2],
        "check_time": data[3],
        "type": data[4],
        "registrer_name": data[5]
    }
    
def attendances_info_schema(data) -> List[AttendanceInfo]:
    return [attendance_info_schema(att) for att in data]
