import { FormEvent, useState } from "react";
import { Link } from "react-router-dom";
import { Code2 } from "lucide-react";
import { forgotPassword } from "@/lib/auth";
import { Card } from "@/components/ui/Card";
import { Input } from "@/components/ui/Input";
import { Button } from "@/components/ui/Button";

export default function ForgotPasswordPage() {
  const [email, setEmail] = useState("");
  const [sent, setSent] = useState(false);
  const [submitting, setSubmitting] = useState(false);

  async function handleSubmit(e: FormEvent) {
    e.preventDefault();
    setSubmitting(true);
    try {
      await forgotPassword(email);
    } finally {
      setSubmitting(false);
      setSent(true);
    }
  }

  return (
    <div className="min-h-screen flex items-center justify-center bg-slate-950 px-6">
      <Card className="w-full max-w-sm">
        <div className="flex items-center gap-2 mb-6 justify-center">
          <Code2 className="w-5 h-5 text-brand-300" />
          <span className="font-semibold tracking-tight">CodeCrack AI</span>
        </div>

        {sent ? (
          <p className="text-sm text-slate-300 text-center">
            If an account exists for <strong>{email}</strong>, a reset link is on its way.
          </p>
        ) : (
          <>
            <h1 className="text-lg font-semibold mb-1">Reset your password</h1>
            <p className="text-sm text-slate-500 mb-6">We'll email you a reset link.</p>
            <form onSubmit={handleSubmit} className="space-y-4">
              <Input
                type="email"
                required
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                placeholder="you@example.com"
              />
              <Button type="submit" className="w-full" disabled={submitting}>
                {submitting ? "Sending..." : "Send reset link"}
              </Button>
            </form>
          </>
        )}

        <p className="text-center mt-4 text-xs text-slate-500">
          <Link to="/login" className="hover:text-slate-300">
            Back to login
          </Link>
        </p>
      </Card>
    </div>
  );
}
