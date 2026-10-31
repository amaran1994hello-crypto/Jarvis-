"""
JARVIS Configuration Settings
"""
import os

class Settings:
    PROJECT_NAME: str = "JARVIS PROFESSIONAL AI"
    FOUNDER_NAME: str = os.getenv("FOUNDER_NAME", "Amar")
    FOUNDER_PHONE_NUMBER: str = os.getenv("FOUNDER_PHONE_NUMBER", "+15557890199")
    HOST: str = "0.0.0.0"
    PORT: int = 8000
    DEBUG: bool = False
    
    GEMINI_API_KEY: str = os.getenv("GEMINI_API_KEY", "")
    TELECOM_DEVICE_API_KEY: str = os.getenv("TELECOM_DEVICE_API_KEY", "")
    
    CORS_ORIGINS: list = ["*"]

settings = Settings()
