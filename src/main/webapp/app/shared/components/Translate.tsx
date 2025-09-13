import React from 'react';
import { translate as translateUtil } from '../util/translation-utils';

/**
 * Translation component to replace react-jhipster Translate component
 */
interface TranslateProps {
  contentKey: string;
  interpolateParams?: Record<string, any>;
  children?: React.ReactNode;
}

export const Translate: React.FC<TranslateProps> = ({ contentKey, interpolateParams, children }) => {
  const translatedText = translateUtil(contentKey, interpolateParams);

  // If translation is found (not the same as key), use it
  // Otherwise, fall back to children or the key itself
  if (translatedText !== contentKey) {
    return <>{translatedText}</>;
  }

  return <>{children || contentKey}</>;
};
