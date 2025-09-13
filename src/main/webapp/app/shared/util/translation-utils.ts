/**
 * Simple translation utilities to replace react-jhipster translation functionality
 */

// In-memory storage for translation messages
let translationData: Record<string, any> = {};

/**
 * Sets the translation data
 */
export const setTranslationData = (data: Record<string, any>) => {
  translationData = data;
};

/**
 * Gets a translated message by key with interpolation support
 */
export const translate = (key: string, interpolateParams?: Record<string, any>): string => {
  const keys = key.split('.');
  let value: any = translationData;

  for (const k of keys) {
    if (value && typeof value === 'object' && k in value) {
      value = value[k];
    } else {
      // Return the key if translation not found
      return key;
    }
  }

  if (typeof value !== 'string') {
    return key;
  }

  // Simple interpolation
  if (interpolateParams) {
    return value.replace(/\{\{\s*(\w+)\s*\}\}/g, (match, paramKey) => {
      return interpolateParams[paramKey] || match;
    });
  }

  return value;
};

/**
 * Storage utility similar to react-jhipster Storage
 */
export const Storage = {
  session: {
    get(key: string, defaultValue?: any) {
      try {
        const item = sessionStorage.getItem(key);
        return item !== null ? JSON.parse(item) : defaultValue;
      } catch {
        return defaultValue;
      }
    },
    set(key: string, value: any) {
      try {
        sessionStorage.setItem(key, JSON.stringify(value));
      } catch {
        // Ignore storage errors
      }
    },
    remove(key: string) {
      try {
        sessionStorage.removeItem(key);
      } catch {
        // Ignore storage errors
      }
    },
  },
  local: {
    get(key: string, defaultValue?: any) {
      try {
        const item = localStorage.getItem(key);
        return item !== null ? JSON.parse(item) : defaultValue;
      } catch {
        return defaultValue;
      }
    },
    set(key: string, value: any) {
      try {
        localStorage.setItem(key, JSON.stringify(value));
      } catch {
        // Ignore storage errors
      }
    },
    remove(key: string) {
      try {
        localStorage.removeItem(key);
      } catch {
        // Ignore storage errors
      }
    },
  },
};

/**
 * Email validation utility
 */
export const isEmail = (value: string): boolean => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(value);
};
