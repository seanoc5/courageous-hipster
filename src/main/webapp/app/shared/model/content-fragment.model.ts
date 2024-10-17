import dayjs from 'dayjs';
import { IThingType } from 'app/shared/model/thing-type.model';
import { IContent } from 'app/shared/model/content.model';
import { IContext } from 'app/shared/model/context.model';
import { ITopic } from 'app/shared/model/topic.model';

export interface IContentFragment {
  id?: number;
  label?: string;
  text?: string | null;
  description?: string | null;
  structuredContent?: string | null;
  startPos?: number | null;
  endPos?: number | null;
  startTermNum?: number | null;
  endTermNum?: number | null;
  subtype?: string | null;
  language?: string | null;
  dateCreated?: dayjs.Dayjs | null;
  lastUpdated?: dayjs.Dayjs | null;
  type?: IThingType | null;
  content?: IContent | null;
  context?: IContext | null;
  topic?: ITopic | null;
}

export const defaultValue: Readonly<IContentFragment> = {};
