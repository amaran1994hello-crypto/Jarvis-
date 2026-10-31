import uvicorn

if __name__ == "__main__":
    print("=" * 60)
    print("⚡ JARVIS PROFESSIONAL AI // FOUNDER: AMAR ⚡")
    print("Telecom Capabilities: CAPABILITY_BASELINE | CAPABILITY_SUPPORTS_VIDEO_CALLING")
    print("API Endpoint: http://localhost:8000")
    print("=" * 60)
    uvicorn.run("backend.main:app", host="0.0.0.0", port=8000, reload=True)
