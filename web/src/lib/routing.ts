import { ApiError } from "@/lib/api";
import { notFound } from "next/navigation";

export const withNextRouting = async <T>(fetchPromise: Promise<T>): Promise<T> => {
  try {
    return await fetchPromise;
  } catch (error) {
    if (error instanceof ApiError && error.status === 404) notFound();
    throw error;
  }
};
