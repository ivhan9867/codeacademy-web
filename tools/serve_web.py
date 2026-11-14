import http.server
import socketserver
import os
import sys

PORT = 3000
DIRECTORY = os.path.join(os.path.dirname(os.path.dirname(os.path.abspath(__file__))), "public")

class Handler(http.server.SimpleHTTPRequestHandler):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, directory=DIRECTORY, **kwargs)

def run():
    os.chdir(DIRECTORY)
    with socketserver.TCPServer(("", PORT), Handler) as httpd:
        print("=" * 60)
        print("  CodeAcademy Web Portal & Facilitator Dashboard Server")
        print("=" * 60)
        print(f"  Root URL         : http://localhost:{PORT}/")
        print(f"  Facilitator Web  : http://localhost:{PORT}/facilitator/")
        print(f"  Certificate Verif: http://localhost:{PORT}/verify.html")
        print("  Press Ctrl+C to stop the server.")
        print("=" * 60)
        try:
            httpd.serve_forever()
        except KeyboardInterrupt:
            print("\nServer stopped.")

if __name__ == "__main__":
    run()
