from config.settings import settings

class GeminiService:
    def __init__(self):
        self.api_key = settings.GEMINI_API_KEY
        self.founder = settings.FOUNDER_NAME

    async def generate_response(self, query: str, user: str = "Amar") -> tuple[str, str | None]:
        q = query.lower()
        if "call amar" in q or "call founder" in q:
            return f"Initiating telecom channel directly to Founder Amar at {settings.FOUNDER_PHONE_NUMBER}.", "TRIGGER_CALL"
        elif "system" in q or "diagnostic" in q or "status" in q:
            return "All systems operational. Core telecom baseline and video calling capabilities registered.", "SHOW_TELEMETRY"
        elif "who are you" in q or "who is amar" in q:
            return f"I am JARVIS Professional AI, architected for Founder {user}. I handle voice synthesis, telephony, and system automations.", None
        elif "reminder" in q:
            return "Reminder protocol acknowledged and queued.", "ADD_REMINDER"
        else:
            return f"Directive received, Mr. {user}: '{query}'. Processing neural routine.", None

gemini_service = GeminiService()
