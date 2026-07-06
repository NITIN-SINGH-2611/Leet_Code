import { create } from "zustand";
import * as authApi from "@/lib/auth";
import type { AuthUser } from "@/lib/auth";

interface AuthState {
  user: AuthUser | null;
  isAuthenticated: boolean;
  isBootstrapping: boolean;
  login: (email: string, password: string) => Promise<void>;
  signup: (fullName: string, email: string, password: string) => Promise<void>;
  logout: () => void;
  bootstrap: () => Promise<void>;
}

export const useAuthStore = create<AuthState>((set) => ({
  user: null,
  isAuthenticated: false,
  isBootstrapping: true,

  login: async (email, password) => {
    const user = await authApi.login(email, password);
    set({ user, isAuthenticated: true });
  },

  signup: async (fullName, email, password) => {
    const user = await authApi.signup(fullName, email, password);
    set({ user, isAuthenticated: true });
  },

  logout: () => {
    authApi.logout();
    set({ user: null, isAuthenticated: false });
  },

  // Called once on app load: if a refresh token exists, try to recover the
  // session by fetching the current profile (the apiClient interceptor
  // transparently refreshes the access token if it's expired).
  bootstrap: async () => {
    const hasRefreshToken = !!authApi.getRefreshToken();
    if (!hasRefreshToken) {
      set({ isBootstrapping: false });
      return;
    }
    try {
      const user = await authApi.fetchMe();
      set({ user, isAuthenticated: true, isBootstrapping: false });
    } catch {
      authApi.clearTokens();
      set({ user: null, isAuthenticated: false, isBootstrapping: false });
    }
  },
}));
