from fastapi import APIRouter
from pydantic import BaseModel
from typing import Optional, List
from backend.services.telecom_service import telecom_service

router = APIRouter()

class CallInitiateRequest(BaseModel):
    phone_number: str
    contact_name: Optional[str] = "Founder Amar"
    is_video: bool = False
    telecom_api_key: Optional[str] = None

class CallSession(BaseModel):
    session_id: str
    status: str
    capabilities: List[str]
    recipient: str

@router.post("/call", response_model=CallSession)
async def initiate_telecom_call(req: CallInitiateRequest):
    session = await telecom_service.start_call(
        phone_number=req.phone_number,
        name=req.contact_name,
        is_video=req.is_video
    )
    return session

@router.get("/status")
async def telecom_status():
    return telecom_service.get_status()
