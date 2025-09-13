import React from 'react';
import { translate as translateUtil } from '../util/translation-utils';

/**
 * Translation component to replace react-jhipster Translate component
 */
interface TranslateProps {
  contentKey: string;
  interpolateParams?: Record<string, any>;
  interpolate?: Record<string, any>; // Support both for compatibility
  children?: React.ReactNode;
}

export const Translate: React.FC<TranslateProps> = ({ contentKey, interpolateParams, interpolate, children }) => {
  const params = interpolateParams || interpolate;
  const translatedText = translateUtil(contentKey, params);

  // If translation is found (not the same as key), use it
  // Otherwise, fall back to children or the key itself
  if (translatedText !== contentKey) {
    return <>{translatedText}</>;
  }

  return <>{children || contentKey}</>;
};
