import { Link } from "react-router-dom";
import { useAsync } from "@/hooks/useAsync";
import { fetchTopics } from "@/lib/api";
import { Card } from "@/components/ui/Card";
import { Skeleton } from "@/components/ui/Skeleton";
import { ProgressBar } from "@/components/ui/ProgressBar";

export default function TopicsPage() {
  const { data: topics, loading, error } = useAsync(fetchTopics);

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold tracking-tight">Topics</h1>
        <p className="text-slate-400 text-sm mt-1">
          Every pattern you need, ordered the way you should actually learn them.
        </p>
      </div>

      {error && (
        <Card className="border-rose-500/30">
          <p className="text-rose-300 text-sm">
            Couldn't reach the backend ({error}). Make sure it's running on port 8080.
          </p>
        </Card>
      )}

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
        {loading &&
          Array.from({ length: 6 }).map((_, i) => <Skeleton key={i} className="h-32" />)}

        {topics?.map((topic) => (
          <Link key={topic.id} to={`/questions?topicId=${topic.id}`}>
            <Card className="hover:bg-white/[0.07] transition-colors h-full">
              <div className="flex items-start justify-between">
                <h3 className="font-semibold">{topic.name}</h3>
                <span className="text-xs text-slate-500">{topic.questionCount} questions</span>
              </div>
              <p className="text-sm text-slate-400 mt-2 line-clamp-2">{topic.description}</p>
              <ProgressBar value={0} className="mt-4" />
            </Card>
          </Link>
        ))}
      </div>
    </div>
  );
}
