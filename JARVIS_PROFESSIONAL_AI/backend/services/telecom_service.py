import uuid
from config.settings import settings

class TelecomService:
    def __init__(self):
        self.capabilities = ["CAPABILITY_BASELINE", "CAPABILITY_SUPPORTS_VIDEO_CALLING"]
        self.registered = True

    def is_registered(self) -> bool:
        return self.registered

    def get_status(self) -> dict:
        return {
            "registered": self.registered,
            "capabilities": self.capabilities,
            "telecom_device_key_configured": bool(settings.TELECOM_DEVICE_API_KEY),
            "founder_phone": settings.FOUNDER_PHONE_NUMBER
        }

    async def start_call(self, phone_number: str, name: str, is_video: bool) -> dict:
        session_id = f"call-{uuid.uuid4().hex[:8]}"
        return {
            "session_id": session_id,
            "status": "CONNECTED",
            "capabilities": self.capabilities,
            "recipient": f"{name} ({phone_number})",
            "video": is_video
        }

telecom_service = TelecomService()
