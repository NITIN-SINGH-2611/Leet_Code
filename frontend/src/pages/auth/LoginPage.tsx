import { FormEvent, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { Code2 } from "lucide-react";
import { useAuthStore } from "@/store/authStore";
import { Card } from "@/components/ui/Card";
import { Input } from "@/components/ui/Input";
import { Button } from "@/components/ui/Button";

export default function LoginPage() {
  const navigate = useNavigate();
  const login = useAuthStore((s) => s.login);

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState<string | null>(null);
  const [submitting, setSubmitting] = useState(false);

  async function handleSubmit(e: FormEvent) {
    e.preventDefault();
    setError(null);
    setSubmitting(true);
    try {
      await login(email, password);
      navigate("/");
    } catch (err: any) {
      setError(err?.response?.data?.message ?? "Invalid email or password");
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <div className="min-h-screen flex items-center justify-center bg-slate-950 px-6">
      <Card className="w-full max-w-sm">
        <div className="flex items-center gap-2 mb-6 justify-center">
          <Code2 className="w-5 h-5 text-brand-300" />
          <span className="font-semibold tracking-tight">CodeCrack AI</span>
        </div>

        <h1 className="text-lg font-semibold mb-1">Welcome back</h1>
        <p className="text-sm text-slate-500 mb-6">Log in to pick up where you left off.</p>

        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="text-xs text-slate-400 mb-1 block">Email</label>
            <Input
              type="email"
              required
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="you@example.com"
            />
          </div>
          <div>
            <label className="text-xs text-slate-400 mb-1 block">Password</label>
            <Input
              type="password"
              required
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="••••••••"
            />
          </div>

          {error && <p className="text-sm text-rose-400">{error}</p>}

          <Button type="submit" className="w-full" disabled={submitting}>
            {submitting ? "Logging in..." : "Log in"}
          </Button>
        </form>

        <div className="flex justify-between mt-4 text-xs text-slate-500">
          <Link to="/forgot-password" className="hover:text-slate-300">
            Forgot password?
          </Link>
          <Link to="/signup" className="hover:text-slate-300">
            Create an account
          </Link>
        </div>
      </Card>
    </div>
  );
}
