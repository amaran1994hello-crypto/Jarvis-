from fastapi import APIRouter
from pydantic import BaseModel
from typing import List
import uuid

router = APIRouter()

class ReminderItem(BaseModel):
    id: str
    title: str
    time: str
    priority: str = "HIGH"
    completed: bool = False

_in_memory_reminders = [
    ReminderItem(id=str(uuid.uuid4()), title="Review JARVIS Deployment with Founder Amar", time="14:00", priority="CRITICAL"),
    ReminderItem(id=str(uuid.uuid4()), title="Verify CallsManager Baseline and Video Calling capabilities", time="16:30", priority="HIGH")
]

@router.get("/", response_model=List[ReminderItem])
async def list_reminders():
    return _in_memory_reminders

@router.post("/", response_model=ReminderItem)
async def create_reminder(item: ReminderItem):
    _in_memory_reminders.append(item)
    return item
