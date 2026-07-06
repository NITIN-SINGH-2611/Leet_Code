import axios from "axios";

export const apiClient = axios.create({
  baseURL: "/api/v1",
  headers: {
    "Content-Type": "application/json",
  },
});

apiClient.interceptors.request.use((config) => {
  const token = localStorage.getItem("cc_access_token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// On a 401, try exactly once to refresh the access token using the refresh
// token, then replay the original request. If refresh itself fails, clear
// tokens so the app falls back to the logged-out state.
let refreshPromise: Promise<string | null> | null = null;

async function refreshAccessToken(): Promise<string | null> {
  const refreshToken = localStorage.getItem("cc_refresh_token");
  if (!refreshToken) return null;

  try {
    const { data } = await axios.post("/api/v1/auth/refresh", { refreshToken });
    const newAccessToken = data.data.accessToken as string;
    localStorage.setItem("cc_access_token", newAccessToken);
    localStorage.setItem("cc_refresh_token", data.data.refreshToken);
    return newAccessToken;
  } catch {
    localStorage.removeItem("cc_access_token");
    localStorage.removeItem("cc_refresh_token");
    return null;
  }
}

apiClient.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;
    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;

      refreshPromise = refreshPromise ?? refreshAccessToken();
      const newToken = await refreshPromise;
      refreshPromise = null;

      if (newToken) {
        originalRequest.headers.Authorization = `Bearer ${newToken}`;
        return apiClient(originalRequest);
      }
    }
    return Promise.reject(error);
  }
);
