import { useState } from "react";
import { useParams, Link } from "react-router-dom";
import { ArrowLeft, Bookmark, BookmarkCheck, CheckCircle2, Lightbulb, BookOpen } from "lucide-react";
import { useAsync } from "@/hooks/useAsync";
import { fetchQuestionBySlug, updateProgress } from "@/lib/api";
import { Card } from "@/components/ui/Card";
import { Badge, DifficultyBadge } from "@/components/ui/Badge";
import { Button } from "@/components/ui/Button";
import { Skeleton } from "@/components/ui/Skeleton";

export default function QuestionDetailPage() {
  const { slug } = useParams<{ slug: string }>();
  const { data: q, loading, error } = useAsync(() => fetchQuestionBySlug(slug!), [slug]);
  const [showHints, setShowHints] = useState(false);
  const [showEditorial, setShowEditorial] = useState(false);
  const [solved, setSolved] = useState(false);
  const [bookmarked, setBookmarked] = useState(false);
  const [saving, setSaving] = useState(false);
  const [actionError, setActionError] = useState<string | null>(null);

  async function handleMarkSolved() {
    if (!q) return;
    setSaving(true);
    setActionError(null);
    try {
      await updateProgress(q.id, { status: "SOLVED" });
      setSolved(true);
    } catch {
      setActionError("Couldn't save that — try again in a moment.");
    } finally {
      setSaving(false);
    }
  }

  async function handleToggleBookmark() {
    if (!q) return;
    const next = !bookmarked;
    setBookmarked(next);
    try {
      await updateProgress(q.id, { bookmarked: next });
    } catch {
      setBookmarked(!next);
      setActionError("Couldn't save that — try again in a moment.");
    }
  }

  if (loading) {
    return (
      <div className="space-y-4 max-w-3xl">
        <Skeleton className="h-8 w-1/2" />
        <Skeleton className="h-40" />
      </div>
    );
  }

  if (error || !q) {
    return (
      <Card className="border-rose-500/30 max-w-xl">
        <p className="text-rose-300 text-sm">Couldn't load this question ({error}).</p>
      </Card>
    );
  }

  return (
    <div className="max-w-3xl space-y-5">
      <Link to="/questions" className="inline-flex items-center gap-1.5 text-sm text-slate-400 hover:text-slate-100">
        <ArrowLeft className="w-4 h-4" /> Back to questions
      </Link>

      <div>
        <div className="flex items-center gap-2 mb-2">
          <DifficultyBadge difficulty={q.difficulty} />
          <Badge tone="brand">{q.pattern}</Badge>
          <Badge>{q.topicName}</Badge>
        </div>
        <h1 className="text-2xl font-bold tracking-tight">{q.title}</h1>
        <div className="flex gap-1.5 mt-2">
          {q.companyTags.map((c) => (
            <Badge key={c}>{c}</Badge>
          ))}
        </div>
      </div>

      <Card>
        <h2 className="font-semibold mb-2">Problem</h2>
        <p className="text-sm text-slate-300 leading-relaxed">{q.problemStatement}</p>
      </Card>

      <div className="grid grid-cols-2 gap-4">
        <Card>
          <p className="text-xs text-slate-500">Time Complexity</p>
          <p className="font-mono text-sm mt-1">{q.timeComplexity ?? "—"}</p>
        </Card>
        <Card>
          <p className="text-xs text-slate-500">Space Complexity</p>
          <p className="font-mono text-sm mt-1">{q.spaceComplexity ?? "—"}</p>
        </Card>
      </div>

      <Card>
        <button
          className="flex items-center gap-2 font-semibold text-sm w-full text-left"
          onClick={() => setShowHints((s) => !s)}
        >
          <Lightbulb className="w-4 h-4 text-amber-400" />
          {showHints ? "Hide hint" : "Reveal hint"}
        </button>
        {showHints && <p className="text-sm text-slate-300 mt-3 leading-relaxed">{q.hints}</p>}
      </Card>

      <Card>
        <button
          className="flex items-center gap-2 font-semibold text-sm w-full text-left"
          onClick={() => setShowEditorial((s) => !s)}
        >
          <BookOpen className="w-4 h-4 text-brand-300" />
          {showEditorial ? "Hide editorial" : "Reveal editorial"}
        </button>
        {showEditorial && <p className="text-sm text-slate-300 mt-3 leading-relaxed">{q.editorial}</p>}
      </Card>

      <div className="flex gap-3 items-center">
        <Button variant={solved ? "secondary" : "primary"} onClick={handleMarkSolved} disabled={saving || solved}>
          <CheckCircle2 className="w-4 h-4" />
          {solved ? "Solved" : saving ? "Saving..." : "Mark Solved"}
        </Button>
        <Button variant="secondary" onClick={handleToggleBookmark}>
          {bookmarked ? <BookmarkCheck className="w-4 h-4" /> : <Bookmark className="w-4 h-4" />}
          {bookmarked ? "Bookmarked" : "Bookmark"}
        </Button>
        {actionError && <span className="text-xs text-rose-400">{actionError}</span>}
      </div>
    </div>
  );
}
