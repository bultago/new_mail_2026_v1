export interface MailFolder {
    id: string;
    name: string;
    type: 'system' | 'user' | 'search' | 'shared' | 'tag';
    icon?: string;
    unreadCount?: number;
    children?: MailFolder[];
}

export interface MailMessage {
    id: string;
    folderId: string;
    subject: string;
    from: { name: string; email: string };
    to: { name: string; email: string }[];
    date: string; // ISO string
    size: string;
    read: boolean;
    flagged: boolean;
    hasAttachment: boolean;
    isUrgent?: boolean; // New field
    tags?: string[];
    preview?: string;
}

export const systemFolders: MailFolder[] = [
    { id: 'inbox', name: 'Inbox', type: 'system', icon: 'inbox', unreadCount: 4 },
    { id: 'sent', name: 'Sent', type: 'system', icon: 'send' },
    { id: 'drafts', name: 'Drafts', type: 'system', icon: 'file', unreadCount: 1 },
    { id: 'reserved', name: 'Reserved', type: 'system', icon: 'clock' },
    { id: 'spam', name: 'Spam', type: 'system', icon: 'alert-octagon', unreadCount: 12 },
    { id: 'trash', name: 'Trash', type: 'system', icon: 'trash-2' },
];

export const userFolders: MailFolder[] = [
    { id: 'project-a', name: 'Project A', type: 'user', unreadCount: 2 },
    { id: 'finance', name: 'Finance', type: 'user' },
];

export const tags: MailFolder[] = [
    { id: 'urgent', name: 'Urgent', type: 'tag', icon: 'tag-red' },
    { id: 'todo', name: 'To Do', type: 'tag', icon: 'tag-blue' },
];

export const mockMessages: MailMessage[] = [
    {
        id: '1',
        folderId: 'inbox',
        subject: 'Project Kickoff Meeting',
        from: { name: 'Alice Smith', email: 'alice@example.com' },
        to: [{ name: 'Me', email: 'me@terracetech.com' }],
        date: '2023-10-27T10:00:00Z',
        size: '25KB',
        read: false,
        flagged: true,
        hasAttachment: true,
        isUrgent: true,
        preview: 'Hi everyone, looking forward to our meeting tomorrow...',
    },
    {
        id: '2',
        folderId: 'inbox',
        subject: 'Weekly Report',
        from: { name: 'Bob Jones', email: 'bob@example.com' },
        to: [{ name: 'Me', email: 'me@terracetech.com' }],
        date: '2023-10-26T14:30:00Z',
        size: '12KB',
        read: true,
        flagged: false,
        hasAttachment: false,
        preview: 'Please find attached the weekly report.',
    },
    {
        id: '3',
        folderId: 'inbox',
        subject: '[Urgent] Service Downtime',
        from: { name: 'Support Team', email: 'support@example.com' },
        to: [{ name: 'All', email: 'all@terracetech.com' }],
        date: '2023-10-27T08:15:00Z',
        size: '5KB',
        read: false,
        flagged: true,
        hasAttachment: false,
        isUrgent: true,
        preview: 'We are experiencing some downtime on server 3...',
    },
    {
        id: '4',
        folderId: 'sent',
        subject: 'Re: Project Kickoff',
        from: { name: 'Me', email: 'me@terracetech.com' },
        to: [{ name: 'Alice Smith', email: 'alice@example.com' }],
        date: '2023-10-27T10:05:00Z',
        size: '2KB',
        read: true,
        flagged: false,
        hasAttachment: false,
        preview: 'Sounds good, see you there.',
    }
];
