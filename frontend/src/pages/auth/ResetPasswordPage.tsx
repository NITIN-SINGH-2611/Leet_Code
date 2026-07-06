import { FormEvent, useState } from "react";
import { Link, useNavigate, useSearchParams } from "react-router-dom";
import { Code2 } from "lucide-react";
import { resetPassword } from "@/lib/auth";
import { Card } from "@/components/ui/Card";
import { Input } from "@/components/ui/Input";
import { Button } from "@/components/ui/Button";

export default function ResetPasswordPage() {
  const [params] = useSearchParams();
  const navigate = useNavigate();
  const token = params.get("token") ?? "";

  const [password, setPassword] = useState("");
  const [error, setError] = useState<string | null>(null);
  const [submitting, setSubmitting] = useState(false);

  async function handleSubmit(e: FormEvent) {
    e.preventDefault();
    setError(null);
    setSubmitting(true);
    try {
      await resetPassword(token, password);
      navigate("/login");
    } catch (err: any) {
      setError(err?.response?.data?.message ?? "That reset link is invalid or expired");
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

        <h1 className="text-lg font-semibold mb-1">Choose a new password</h1>
        <p className="text-sm text-slate-500 mb-6">At least 8 characters.</p>

        <form onSubmit={handleSubmit} className="space-y-4">
          <Input
            type="password"
            required
            minLength={8}
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            placeholder="New password"
          />
          {error && <p className="text-sm text-rose-400">{error}</p>}
          <Button type="submit" className="w-full" disabled={submitting || !token}>
            {submitting ? "Updating..." : "Update password"}
          </Button>
        </form>

        <p className="text-center mt-4 text-xs text-slate-500">
          <Link to="/login" className="hover:text-slate-300">
            Back to login
          </Link>
        </p>
      </Card>
    </div>
  );
}
