import argparse
import http.server
import os
import socketserver

DEFAULT_PORT = 3000
DIRECTORY = os.path.join(os.path.dirname(os.path.dirname(os.path.abspath(__file__))), "public")


class ReusableTCPServer(socketserver.TCPServer):
    allow_reuse_address = True


class Handler(http.server.SimpleHTTPRequestHandler):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, directory=DIRECTORY, **kwargs)

    def end_headers(self):
        self.send_header("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0")
        self.send_header("Pragma", "no-cache")
        super().end_headers()


def run():
    parser = argparse.ArgumentParser(description="Serve CodeAcademy Web Portal")
    parser.add_argument("--port", type=int, default=DEFAULT_PORT, help="Port to listen on (default: 3000)")
    args = parser.parse_args()
    port = args.port

    os.chdir(DIRECTORY)
    with ReusableTCPServer(("", port), Handler) as httpd:
        print("=" * 60)
        print("  CodeAcademy Web Portal & Facilitator Dashboard Server")
        print("=" * 60)
        print(f"  Root URL         : http://localhost:{port}/")
        print(f"  Facilitator Web  : http://localhost:{port}/facilitator/")
        print(f"  Certificate Verif: http://localhost:{port}/verify.html")
        print("  Press Ctrl+C to stop the server.")
        print("=" * 60)
        try:
            httpd.serve_forever()
        except KeyboardInterrupt:
            print("\nServer stopped.")


if __name__ == "__main__":
    run()
