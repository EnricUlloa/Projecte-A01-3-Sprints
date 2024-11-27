from typing import List
from api.classes import User, UserInfo

def user_info_schema(data) -> User:
    return {
        "id": data[0],
        "email": data[1],
        "full_name": data[2],
        "rol":data[3],
        "groups":data[4].split(",")
    }
    
def users_info_schema(data) -> List[User]:
    return [user_info_schema(raw_user) for raw_user in data]
