import { useState } from "react";
import { Link, useSearchParams } from "react-router-dom";
import { Search } from "lucide-react";
import { useAsync } from "@/hooks/useAsync";
import { fetchQuestions } from "@/lib/api";
import type { Difficulty } from "@/types";
import { Card } from "@/components/ui/Card";
import { Input } from "@/components/ui/Input";
import { Button } from "@/components/ui/Button";
import { Badge, DifficultyBadge } from "@/components/ui/Badge";
import { Skeleton } from "@/components/ui/Skeleton";

const DIFFICULTIES: Difficulty[] = ["EASY", "MEDIUM", "HARD"];

export default function QuestionsPage() {
  const [params] = useSearchParams();
  const topicId = params.get("topicId") ? Number(params.get("topicId")) : undefined;

  const [search, setSearch] = useState("");
  const [difficulty, setDifficulty] = useState<Difficulty | undefined>(undefined);
  const [page, setPage] = useState(0);

  const { data, loading, error } = useAsync(
    () => fetchQuestions({ topicId, difficulty, search: search || undefined, page, size: 12 }),
    [topicId, difficulty, search, page]
  );

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold tracking-tight">Questions</h1>
        <p className="text-slate-400 text-sm mt-1">
          The 20% that shows up again and again in real interviews.
        </p>
      </div>

      <div className="flex flex-col sm:flex-row gap-3">
        <div className="relative flex-1">
          <Search className="w-4 h-4 text-slate-500 absolute left-3 top-1/2 -translate-y-1/2" />
          <Input
            placeholder="Search by title..."
            className="pl-9"
            value={search}
            onChange={(e) => {
              setPage(0);
              setSearch(e.target.value);
            }}
          />
        </div>
        <div className="flex gap-2">
          <Button
            variant={difficulty === undefined ? "primary" : "secondary"}
            size="sm"
            onClick={() => {
              setPage(0);
              setDifficulty(undefined);
            }}
          >
            All
          </Button>
          {DIFFICULTIES.map((d) => (
            <Button
              key={d}
              variant={difficulty === d ? "primary" : "secondary"}
              size="sm"
              onClick={() => {
                setPage(0);
                setDifficulty(d);
              }}
            >
              {d.charAt(0) + d.slice(1).toLowerCase()}
            </Button>
          ))}
        </div>
      </div>

      {error && (
        <Card className="border-rose-500/30">
          <p className="text-rose-300 text-sm">
            Couldn't reach the backend ({error}). Make sure it's running on port 8080.
          </p>
        </Card>
      )}

      <div className="space-y-3">
        {loading && Array.from({ length: 4 }).map((_, i) => <Skeleton key={i} className="h-20" />)}

        {data?.content.map((q) => (
          <Link key={q.id} to={`/questions/${q.slug}`}>
            <Card className="hover:bg-white/[0.07] transition-colors flex items-center justify-between gap-4">
              <div className="min-w-0">
                <div className="flex items-center gap-2 mb-1.5">
                  <DifficultyBadge difficulty={q.difficulty} />
                  <Badge>{q.pattern}</Badge>
                  <Badge tone="brand">{q.topicName}</Badge>
                </div>
                <h3 className="font-medium truncate">{q.title}</h3>
                <div className="flex gap-1.5 mt-1.5">
                  {q.companyTags.slice(0, 4).map((c) => (
                    <Badge key={c}>{c}</Badge>
                  ))}
                </div>
              </div>
              <div className="text-right shrink-0 text-xs text-slate-500">
                <p>Frequency {q.frequency}/5</p>
                <p>Importance {q.importanceScore}</p>
              </div>
            </Card>
          </Link>
        ))}

        {data && data.content.length === 0 && (
          <Card>
            <p className="text-sm text-slate-400">No questions match those filters yet.</p>
          </Card>
        )}
      </div>

      {data && data.totalPages > 1 && (
        <div className="flex items-center justify-center gap-3">
          <Button variant="secondary" size="sm" disabled={page === 0} onClick={() => setPage((p) => p - 1)}>
            Previous
          </Button>
          <span className="text-sm text-slate-400">
            Page {page + 1} of {data.totalPages}
          </span>
          <Button
            variant="secondary"
            size="sm"
            disabled={page + 1 >= data.totalPages}
            onClick={() => setPage((p) => p + 1)}
          >
            Next
          </Button>
        </div>
      )}
    </div>
  );
}
