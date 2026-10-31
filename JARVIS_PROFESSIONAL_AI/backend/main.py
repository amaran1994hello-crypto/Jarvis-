"""
JARVIS PROFESSIONAL AI - Core Server
Founder: Amar
"""
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from config.settings import settings
from backend.routes import chat, telecom, reminders
from backend.services.telecom_service import telecom_service

app = FastAPI(
    title="JARVIS PROFESSIONAL AI",
    description="Advanced Personal AI Assistant for Founder Amar with Voice, Telephony, and Automation",
    version="4.0.0"
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=settings.CORS_ORIGINS,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

app.include_router(chat.router, prefix="/api/v1/chat", tags=["Neural Chat"])
app.include_router(telecom.router, prefix="/api/v1/telecom", tags=["Telecom & Calls"])
app.include_router(reminders.router, prefix="/api/v1/reminders", tags=["Reminders"])

@app.get("/")
async def root():
    return {
        "system": "JARVIS PROFESSIONAL AI",
        "founder": settings.FOUNDER_NAME,
        "status": "OPERATIONAL",
        "telecom_capabilities": ["CAPABILITY_BASELINE", "CAPABILITY_SUPPORTS_VIDEO_CALLING"],
        "version": "4.0.0"
    }

@app.get("/api/v1/health")
async def health_check():
    return {
        "status": "HEALTHY",
        "telecom_registered": telecom_service.is_registered(),
        "founder": settings.FOUNDER_NAME,
        "core": "OPTIMAL"
    }
