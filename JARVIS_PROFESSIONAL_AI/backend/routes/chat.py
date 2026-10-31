from fastapi import APIRouter
from pydantic import BaseModel
from typing import Optional
from backend.services.gemini_service import gemini_service
from config.settings import settings

router = APIRouter()

class ChatRequest(BaseModel):
    query: str
    is_voice: bool = False
    founder: Optional[str] = settings.FOUNDER_NAME

class ChatResponse(BaseModel):
    reply: str
    action: Optional[str] = None
    voice_pitch: float = 0.95
    voice_speed: float = 1.05

@router.post("/query", response_model=ChatResponse)
async def process_chat(req: ChatRequest):
    response_text, action = await gemini_service.generate_response(req.query, req.founder)
    return ChatResponse(reply=response_text, action=action)
