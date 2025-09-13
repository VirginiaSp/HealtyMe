/**
 * Translation utilities to replace react-jhipster translation functionality
 */

// Note: In a real app, this would be loaded dynamically
// For now, we'll use a simple fallback mechanism
const translations = {};

// Simple translation data store - in a real app this would be more sophisticated

export const translate = (key: string, interpolateParams?: Record<string, any>): string => {
  // For now, return the key itself as a fallback
  // In a real implementation, this would load from actual translation files
  return key;
};

// Simple storage utility to replace react-jhipster Storage
export class Storage {
  static session = {
    get: (key: string): string | null => sessionStorage.getItem(key),
    set: (key: string, value: string): void => sessionStorage.setItem(key, value),
    remove: (key: string): void => sessionStorage.removeItem(key),
    clear: (): void => sessionStorage.clear(),
  };

  static local = {
    get: (key: string): string | null => localStorage.getItem(key),
    set: (key: string, value: string): void => localStorage.setItem(key, value),
    remove: (key: string): void => localStorage.removeItem(key),
    clear: (): void => localStorage.clear(),
  };
}
